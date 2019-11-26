#!/bin/bash

INPUT="/thesis/small_sep.txt"
OUTPUT="/thesis/results"
RESULTS="results.txt"
ITERCOUNT="iter_count.txt"
K=4
T1=6
T2=3
ITER=10


$HADOOP_HOME/bin/hdfs dfsadmin -safemode leave

mahout org.apache.mahout.clustering.syntheticcontrol.kmeans.Job --input $INPUT --t1 $T1 --t2 $T2 --maxIter $ITER --output $OUTPUT --numClusters $K --overwrite

mahout clusterdump --input $OUTPUT/*final --pointsDir $OUTPUT/clusteredPoints --output $RESULTS

hadoop fs -ls $OUTPUT > $ITERCOUNT

