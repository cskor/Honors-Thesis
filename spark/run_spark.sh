#!/bin/bash

INPUT="hdfs://nashville:30841/thesis/pract.txt"
OUTPUT="hdfs://nashville:30841/thesis/cluster_centers"

sbt package
$HADOOP_HOME/bin/hdfs dfsadmin -safemode leave
$HADOOP_HOME/bin/hadoop fs -rm -r $OUTPUT
$SPARK_HOME/bin/spark-submit --driver-java-options "-Dlog4j.configuration=file:/s/bach/d/under/cskor/spark/conf/driver_log4j.properties" --master spark://nashville:30860  --deploy-mode cluster --class runKMeans --supervise target/scala-2.11/honors-thesis_2.11-1.0.jar $INPUT $OUTPUT
$HADOOP_HOME/bin/hadoop fs -rm $OUTPUT/_*
