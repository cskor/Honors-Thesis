import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.mllib.clustering.{KMeans, KMeansModel}
import org.apache.spark.mllib.linalg.Vectors

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import java.io.PrintWriter;

object runKMeans {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAppName("Honors Thesis")
    val sc = new SparkContext(conf)

    // Load and parse the data
    val data = sc.textFile(args(0))
    val parsedData = data.map(s => Vectors.dense(s.split(' ').map(_.toDouble))).cache()

    // Cluster the data into two classes using KMeans
    val numClusters = 5
    val numIterations = 35
    val model = new KMeans()
    
    //Get the first clusters
    model.setK(numClusters).setMaxIterations(0)
    val startingClusters = model.run(parsedData)    
    sc.parallelize( startingClusters.clusterCenters ).coalesce(1).saveAsTextFile(args(1))

    //Run the algorithm
    model.setK(numClusters).setMaxIterations(numIterations)
    val clusters = model.run(parsedData)
    
    // Evaluate clustering by computing Within Set Sum of Squared Errors
    val WSSSE = clusters.trainingCost
    println(s"Within Set Sum of Squared Errors = $WSSSE")

    sc.stop()
  }
}
