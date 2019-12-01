import numpy as np
from matplotlib import pyplot as plt
from mpl_toolkits.mplot3d import Axes3D
from collections import defaultdict
import sys

def sparkGatherCanopies(centerFile, pointsFile):
    with open(centerFile) as f:
        centers = {}
        for line in f:
            values = line.strip().split('],')
            index = int( values[1][:-1] )
            coord = values[0][2:]
            centers[index] = coord
    with open(pointsFile) as f:
        points = defaultdict(list)
        for line in f:
            values = line.strip().split(",[")
            index = int(values[0][1:])
            coord = values[1][:-2]
            points[index].append(coord)
    canopies = {}
    for k, v in centers.items():
        canopies[v] = points[k]
    return canopies

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

def sparkResults(filename, output, inputName):
    with open(filename) as f:
        current = f.readlines()[-4:]
        buildTime = current[0].strip().split(" ")[-2] 
        iterTime = current[1].strip().split(" ")[-2] 
        iterations = current[2].strip().split(" ")[-2] 
        cost = current[3].strip()[:-1].split(" ")[-1]
    with open(output, 'a+') as f:
        f.write(inputName + ",spark," + buildTime + "," + iterTime + "," + iterations + "," + cost + ",\n")

if __name__ == "__main__":
    # args = [ centers.txt, points.txt, sparkdriver.log, results.csv", input file name,  plot if present]

    sparkCanopy = sparkGatherCanopies( sys.argv[1], sys.argv[2])
    
    sparkResults( sys.argv[3], sys.argv[4], sys.argv[5] )
    
    if len(sys.argv) == 7:
        plotClusters(sparkCanopy, "Spark")

    print("FINISHED PROCESSING SPARK")

