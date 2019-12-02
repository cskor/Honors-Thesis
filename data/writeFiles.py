import numpy as np
import sys
import math

def numberPoints(goal, dim):
    #CALCULATES HOW MANY POINTS WE NEED BASED ON POINT DIMENSIONS AND TARGET GB SIZE
    GB = 1024**3
    WRITE_PT = 18
    PERCENTAGE = .15
    
    ptsNeed = goal*GB / WRITE_PT
    pairsNeed = ptsNeed / dim
    
    small = math.ceil( pairsNeed * PERCENTAGE)
    big = math.floor( pairsNeed * (1 - PERCENTAGE))
    return small, big

def writefile(goal, dim, filename):
    smallCount, bigCount = numberPoints(goal, dim)
    with open(filename, "w") as f:
        for _ in range(smallCount):
            l = np.random.normal(3, 1, dim).tolist()
            f.write( ' '.join(map(str, l)) + "\n")
        for _ in range(bigCount):
            l = np.random.normal(5, 1, dim).tolist()
            f.write( ' '.join(map(str, l)) + "\n")

if __name__ == "__main__":
    #args = [goal, dim, filename]
    writefile( float(sys.argv[1]), int(sys.argv[2]), sys.argv[3]) 
