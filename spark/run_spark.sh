#!/bin/bash

#INPUTFILE="data/fraud/ugh.txt"
#INPUT="hdfs://nashville:30841/thesis"/$INPUTFILE
OUTPUT="hdfs://nashville:30841/thesis/spark_results"
K=2
ITER=35
PLOT=0

#Build Scala Code
sbt package

#Start the Spark Job
$HADOOP_HOME/bin/hdfs dfsadmin -safemode leave
$HADOOP_HOME/bin/hadoop fs -rm -r $OUTPUT

for i in 0 1 2 3 4 5 6 7 8 9 10 11 12 13
do
    INPUTFILE="data/fraud/$i-fraud.txt"
    INPUT="hdfs://nashville:30841/thesis"/$INPUTFILE
    echo "doing $INPUTFILE"
    
    if [ $PLOT -gt 0 ]
    then
        echo "PLOT"
        $SPARK_HOME/bin/spark-submit --driver-java-options "-Dlog4j.configuration=file:/s/bach/d/under/cskor/spark/conf/driver_log4j.properties" --master yarn  --deploy-mode cluster --class runKMeans --supervise target/scala-2.11/honors-thesis_2.11-1.0.jar $INPUT $K $ITER $OUTPUT/points $OUTPUT/centers $PLOT

        #Capture the Output Files
        hadoop fs -copyToLocal $OUTPUT/points/part-00000 .
        mv part-00000 points.txt

        hadoop fs -copyToLocal $OUTPUT/centers/part-00000 .
        mv part-00000 centers.txt

        #Process results -- add them to the results.csv [add plot if we want to plot]
        python process_spark.py centers.txt points.txt SparkDriver.log ../results.csv $INPUTFILE $PLOT
    else
        echo "NO PLOT"
        $SPARK_HOME/bin/spark-submit --driver-java-options "-Dlog4j.configuration=file:/s/bach/d/under/cskor/spark/conf/driver_log4j.properties" --master spark://nashville:30860  --deploy-mode cluster --class runKMeans --supervise target/scala-2.11/honors-thesis_2.11-1.0.jar $INPUT $K $ITER 
        
        #Process results -- add them to the results.csv 
        python process_spark.py centers.txt points.txt SparkDriver.log ../results.csv $INPUTFILE 
    fi
done
