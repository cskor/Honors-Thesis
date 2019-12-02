#!/bin/bash

GOAL_GB=(0.25 0.5 0.75 1 1.5 2 3 4 5 6 7 8 9 10)
NAME="fraud"
DIM=5000

hadoop fs -mkdir /thesis/data/temp
# 0 1 2 3 4 5 6 7 8 9 10 11 12 13
for i in 10  
do
    #python writeFiles.py ${GOAL_GB[$i]} $DIM $i-$NAME.txt
    python writeFiles.py 1 $DIM $i-$NAME.txt
    hadoop fs -put $i-$NAME.txt /thesis/data/temp
    rm $i-$NAME.txt
    echo "Finished $i"
done
