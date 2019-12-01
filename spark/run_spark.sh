#!/bin/bash

INPUTFILE="small_sep.txt"
INPUT="hdfs://nashville:30841/thesis"/$INPUTFILE
OUTPUT="hdfs://nashville:30841/thesis/spark_results"
K=4
ITER=35

#Build Scala Code
sbt package

#Start the Spark Job
$HADOOP_HOME/bin/hdfs dfsadmin -safemode leave
$HADOOP_HOME/bin/hadoop fs -rm -r $OUTPUT
$SPARK_HOME/bin/spark-submit --driver-java-options "-Dlog4j.configuration=file:/s/bach/d/under/cskor/spark/conf/driver_log4j.properties" --master yarn  --deploy-mode cluster --class runKMeans --supervise target/scala-2.11/honors-thesis_2.11-1.0.jar $INPUT $K $ITER $OUTPUT/points $OUTPUT/centers

#Capture the Output Files
hadoop fs -copyToLocal $OUTPUT/points/part-00000 .
mv part-00000 points.txt

hadoop fs -copyToLocal $OUTPUT/centers/part-00000 .
mv part-00000 centers.txt

#Process results -- add them to the results.csv [add plot if we want to plot]
python process_spark.py centers.txt points.txt SparkDriver.log ../results.csv $INPUTFILE plot

