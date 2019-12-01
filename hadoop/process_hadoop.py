import numpy as np
from matplotlib import pyplot as plt
from mpl_toolkits.mplot3d import Axes3D
from collections import defaultdict
import sys

def hadoopGatherCanopies(filename):
    #GATHER CLUSTER DATA POINTS
    with open(filename) as f:
        clusters = {}
        for line in f:
            if line[0] is '{':
                center = line.split('"c":[')[1]
                center = center.split("]")[0]
                clusters[center] = []
            elif 'distance' in line:
                values = line.strip().split(': [')[-1][:-1]
                clusters[center].append(values)
    return clusters

def hadoopIterations(filename):
    #TAKES OUTPUT FILE AND GRABS ITER COUNT
    with open(filename) as f:
        finalLine = ""
        for line in f:
            if "final" in line:
                finalLine = line
    value = finalLine.split("-")[-2]
    return int(value) + 1

def hadoopError(filename):
    with open(filename) as f:
        error = 0
        for line in f:
            if 'distance' in line:
                value = line.split("distance=")[1]
                value = value.split(']:')[0]
                error += float(value)
    return error

def plotClusters(canopies, name):
    #PLOTS HADOOP/SPARK CLUSTERS --ONLY USE IF R^3
    fig = plt.figure()
    ax = fig.add_subplot(111, projection='3d')
    ax.set_xlabel('x')
    ax.set_ylabel('y')
    ax.set_zlabel('z')

    for k, v in canopies.items():
        x,y,z = [], [], []
        for pt in v:
            X,Y,Z = list(map( float, pt.split(',') ) )
            x.append(X)
            y.append(Y)
            z.append(Z)
        ax.scatter(x, y, z)

    plt.title(name + " Clusters")
    plt.savefig(name + ".png")
    
def writeHadoopResults(error, iterations, outputFile, inputName):
    with open(outputFile, 'a+') as f:
        f.write(inputName + ",hadoop,-,-," + iterations + "," + error + ",\n")

if __name__ == "__main__":
    # args = [ results.txt,  iter_count.txt, results.csv, input file name, plot if present]

    hadoopClusters = hadoopGatherCanopies( sys.argv[1] )
    
    error = str( hadoopError( sys.argv[1] ) )
    
    iterations = str( hadoopIterations(sys.argv[2]) )
    
    writeHadoopResults( error, iterations, sys.argv[3], sys.argv[4] )
    
    if len(sys.argv) == 6:
        plotClusters(hadoopClusters, "Hadoop")

    print("FINISHED PROCESSING HADOOP")
