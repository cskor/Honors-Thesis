INPUT="/thesis/cluster_centers"
OUTPUT="/thesis/cluster_centers_seqdir"

gradle shadowJar
$HADOOP_HOME/bin/hdfs dfsadmin -safemode leave
$HADOOP_HOME/bin/hadoop fs -rm -r $OUTPUT
$HADOOP_HOME/bin/hadoop jar build/libs/Honors-Thesis.jar ConvertText.ConvertTextJob $INPUT $OUTPUT

