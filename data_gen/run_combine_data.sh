#!/bin/bash

INPUT=/thesis/data/temp
OUTPUT=/thesis/data/temp/results

gradle build

$HADOOP_HOME/bin/hdfs dfsadmin -safemode leave
$HADOOP_HOME/bin/hadoop fs -rm -r $OUTPUT
$HADOOP_HOME/bin/hadoop jar build/libs/Honors_Thesis.jar DataGen.DataGenJob $INPUT $OUTPUT
