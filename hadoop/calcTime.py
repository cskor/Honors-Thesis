import sys

def calcElaspedTime(minA, secA, minB, secB):
    #CALCULATES ELAPSED TIME TAKEN FROM YARN UI
    if minA == minB:
        return secB - secA
    
    AtoMinute = 60 - secA
    MinDiff = minB - minA - 1
    return AtoMinute + MinDiff*60 + secB

if __name__ == "__main__":
    #argv = [minA, secA, minB, secB]

    print( calcElaspedTime( int(sys.argv[1]), int(sys.argv[2]), int(sys.argv[3]), int(sys.argv[4])) )
