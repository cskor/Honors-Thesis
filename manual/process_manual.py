import numpy as np
from matplotlib import pyplot as plt
import pandas as pd
from IPython.display import display
from ipywidgets import FloatProgress
from collections import defaultdict
from mpl_toolkits.mplot3d import Axes3D
import math
import random
import time
import sys

def readInFile(filename):
    with open(filename) as f:
        datapts = []
        for line in f:
            datapts.append(list(map(float, line.split(" "))))
    return datapts

def canopyAlgo(datapts, tight, loose):
    #Create a set of the points we can randomly choose from
    potentialPts = set(range(len(datapts)))

    #These are the randomly chosen center points
    centers = []
    canopies = dict()
    
    #These are the points that are within the loose distance
    loosePts = set()
    
    while potentialPts.difference(loosePts):
        #Randomly select a point to make its own cluster
        centerIndex = random.sample(potentialPts, 1)[0]
        potentialPts.remove(centerIndex)
        centers.append(centerIndex)
        canopies[centerIndex] = centerIndex
        
        #Points to remove
        tightPts = set()
        
        #Check each point distance 
        for pointIndex in potentialPts:
            distance = computeDistance(datapts[pointIndex], datapts[centerIndex])
            
            if distance < tight:
                tightPts.add(pointIndex)
                canopies[pointIndex] = centerIndex
                
            elif distance < loose:
                loosePts.add(pointIndex)
                canopies[pointIndex] = centerIndex
                
        potentialPts = potentialPts.difference(tightPts)
        
    return centers, canopies
            
def computeDistance(ptA, ptB):
    return sum( [(a-b)**2 for a,b in zip(ptA, ptB)])

def assignToCluster(datapts, centers, tight, loose):
    canopies = {}
    
    for pointIndex in range( len(datapts) ):
        distances = []
        #Find the distance to each center point and take the lowest as our cluster center
        for centerIndex in centers:
            distances.append( computeDistance(datapts[pointIndex], datapts[centerIndex]) )
        
        minIndex = distances.index(min(distances))
        canopies[pointIndex] = centers[minIndex]
        
    return canopies

def kStartingPoints(k, datapts, tight, loose):
    centers, canopies = canopyAlgo(datapts, tight, loose)
    newTight = tight
    newLoose = loose
    while len(centers) != k:

        if len(centers) > k:
            #Too many clusters -- run again on the centers only 
            newDatapts = []
            for center in centers:
                newDatapts.append(datapts[center])
            newTight*=1.5
            newLoose*=1.5
            centers, _ = canopyAlgo(newDatapts, newTight, newLoose)
            canopies = assignToCluster(datapts, centers, newTight, newLoose)
        else:
            #too few clusters -- run again with smaller bounds
            newTight*=.75
            newLoose*=.75
            centers, canopies = canopyAlgo(datapts, newTight, newLoose)
            
    return centers, canopies

def kmeansAlgorithm(centers, datapts, iterations):
    for i in range(iterations):
        distances = []
        
        #GENERATE NEW CLUSTERS
        currentCanopies = defaultdict(list)
        for pointIndex in range( len(datapts) ):
            distances = []
            #FIND THE DISTANCE TO EACH CENTER AND TAKE LOWEST AS YOUR CLUSTER CENTER
            for centerIndex in centers:
                distances.append( computeDistance(datapts[pointIndex], datapts[centerIndex]) )
        
            minIndex = distances.index(min(distances))
            currentCanopies[centers[minIndex]].append(pointIndex)
        
        #FIND THE NEW CENTERS OF THE CLUSTERS
        newCenters = []
        for center, pts in currentCanopies.items():
            lowestAverage = float('inf')
            lowestIndex = -1
            
            for currentPt in pts:
                average = 0
                for comparePt in pts:
                    average += computeDistance(datapts[currentPt], datapts[comparePt] )
                average /= len(pts)
                
                if average < lowestAverage:
                    lowestAverage = average
                    lowestIndex = currentPt
            newCenters.append(lowestIndex)
        
        #REPEAT UNTIL CENTERS DO NOT CHANGE OR ITERATIONS FINISH
        centersHaveDifferentPts = [x for x in centers if x not in newCenters] 
        if centersHaveDifferentPts:
            centers = newCenters
        else:
            return currentCanopies, i+1
    return currentCanopies, iterations

def calculateError(currentCanopies, datapts):
    error = 0
    for centerIndex, pts in currentCanopies.items():
        for pointIndex in pts:
            error += computeDistance(datapts[pointIndex], datapts[centerIndex])
    return error

def plotClusters(currentCanopies, datapts):
    fig = plt.figure()
    ax = fig.add_subplot(111, projection='3d')
    ax.set_xlabel('x')
    ax.set_ylabel('y')
    ax.set_zlabel('z')

    for centerIndex, pts in currentCanopies.items():
        x,y,z = [], [], []
        for pointIndex in pts:
            X,Y,Z = list(map( float, datapts[pointIndex] ) )
            x.append(X)
            y.append(Y)
            z.append(Z)
        ax.scatter(x, y, z)

    plt.title("Manual Clusters")
    plt.savefig("manual.png")
    
def runExperiment(filename, k, tight, loose, iterations, outputFile, plotPts=False):
    #READ IN THE DATA POINTS
    datapts = readInFile(filename)
    
    #FIND STARTING POINTS
    start = time.time()
    centers, startCanopy = kStartingPoints(k, datapts, tight, loose)
    clusterTime = time.time() - start
    
    #RUN KMEANS ALGORITHM
    start = time.time()
    currentCanopies, iterations = kmeansAlgorithm(centers, datapts, iterations)
    iterationTime = time.time() - start
    
    #CALCULATE ERROR
    error = calculateError( currentCanopies, datapts )
    
    #SAVE TO OUTPUT FILE
    with open(outputFile, 'a+') as f:
        f.write(filename + ',manual,' + str(clusterTime) + "," + str(iterationTime) + "," + \
                str(iterations) + "," + str(error) + ",\n")
    print("FINISHED PROCESSING MANUAL")
    
    if plotPts:
        plotClusters(currentCanopies, datapts)
        
if __name__ == "__main__":
    #args = [inputfile, k, tight, loose, maxIter, outputfile,  plot if present]
    
    plot = len(sys.argv) == 8
    runExperiment(sys.argv[1], int(sys.argv[2]), float(sys.argv[3]), float(sys.argv[4]), int(sys.argv[5]), sys.argv[6],  plotPts=plot)
