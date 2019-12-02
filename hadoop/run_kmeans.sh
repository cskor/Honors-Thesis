#!/bin/bash

#INPUTFILE="small_sep.txt"
#INPUT="/thesis"/$INPUTFILE
OUTPUT="/thesis/results"
#RESULTS="results.txt"
#ITERCOUNT="iter_count.txt"
K=2
T1=6
T2=3
ITER=35


$HADOOP_HOME/bin/hdfs dfsadmin -safemode leave

for i in 0 1 2 3 4 5 6 7 8 9 10 11 12 13
do
    INPUTFILE="data/fraud/$i-fraud.txt"
    INPUT="hdfs://nashville:30841/thesis"/$INPUTFILE
    RESULTS="$i-results.txt"
    ITERCOUNT="$i-iter_count.txt"
    echo "doing $INPUTFILE"
  
    #Run the job to cluster and get results
    mahout org.apache.mahout.clustering.syntheticcontrol.kmeans.Job --input $INPUT --t1 $T1 --t2 $T2 --maxIter $ITER --output $OUTPUT --numClusters $K --overwrite | hadoop fs -put - /thesis/hadoopResults/temp
    
    #Rename the results
    hadoop fs -mv /thesis/hadoopResults/temp/- /thesis/hadoopResults/temp/$RESULTS
    #mahout clusterdump --input $OUTPUT/*final --pointsDir $OUTPUT/clusteredPoints --output $RESULTS

    #Save the iterations
    hadoop fs -ls $OUTPUT > $ITERCOUNT

    #Process the results
    #python process_hadoop.py $RESULTS $ITERCOUNT ../results.csv $INPUTFILE 
done
