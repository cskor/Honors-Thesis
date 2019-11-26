#!/bin/bash

OUTPUT="hdfs://nashville:30841/thesis/spark_results"

hadoop fs -copyToLocal $OUTPUT/points/part-00000 .
mv part-00000 points.txt

hadoop fs -copyToLocal $OUTPUT/centers/part-00000 .
mv part-00000 centers.txt
