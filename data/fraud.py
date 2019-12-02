import os
import numpy as np
import pandas as pd

def one_big_one_small(filename, dim, bigCount, bigCenter, bigRadius, smallCount, smallCenter, smallRadius):
    with open(filename, "w") as f:
        #LARGE CLUSTER DATA POINTS
        for _ in range(bigCount):
            l = np.random.normal(bigCenter, bigRadius, dim).tolist()
            f.write( ' '.join(map(str, l)) + "\n")
        
        #SMALL CLUSTER DATA POINTS
        for _ in range(smallCount):
            r = np.random.normal(smallCenter, smallRadius, dim).tolist()
            f.write( ' '.join(map(str, r))  + "\n")
            
def generateFraudFiles():
    #WILL TELL US WHAT FILE HAS WHAT CHARACTERISTICS
    results = pd.DataFrame(columns=['Filename', 'Dimension', 'Big Count', 'Big Center', 'Big Radius', \
                                         'Small Count', 'Small Center', 'Small Radius', 'Size (Gb)'])
    
    #FACTORS TO TEST
    dimensions = [3, 1000, 10000]
    bigCount = [1000, 5000, 10000]
    smallCount = [100, 500, 1000]
    bigOrientation = [ (5,1),]# (5,.5), (5, 2)]
    smallOrientation = [ (3,1),]# (3, .5), (3,2)]
    
    #ALL POSSIBLE COMBINATIONS TO GENERATE TEXT FILES
    combos = [[dim, bC, bO, sC, sO] for dim in dimensions for bC in bigCount for bO in bigOrientation \
                                     for sC in smallCount for sO in smallOrientation]
    
    filename = "one_big_one_small"
    for i, [dim, bC, bO, sC, sO] in enumerate(combos):
        
        name = filename + "_" + str(i) + ".txt"
        one_big_one_small(name, dim, bC, bO[0], bO[1], sC, sO[0], sO[1])
        size = os.stat(name).st_size / (10**9)
        results.loc[len(results)] = [name, dim, bC, bO[0], bO[1], sC, sO[0], sO[1], size]

    
    #SAVE THE RESULTS AS PICKLE FILE
    results.to_pickle("./" + filename + ".pkl")
    
def plz():
    f = open("ugh.txt", "w")
    for _ in range(15211):
        l = np.random.normal(3, 1, 5000).tolist()
        f.write( ' '.join(map(str, l)) + "\n")
    f.close()

    f = open("ugh.txt", 'a+')
    for _ in range(2685):
        r = np.random.normal(5, 1, 5000).tolist()
        f.write( ' '.join(map(str, r))  + "\n")
    f.close()
    
if __name__ == "__main__":
    #generateFraudFiles()
    plz()
    print("DONE")
