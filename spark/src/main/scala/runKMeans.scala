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
    val numClusters = args(1).toInt
    val numIterations = args(2).toInt
    val model = new KMeans()
    
    //Run the algorithm
    model.setK(numClusters).setMaxIterations(numIterations)
    val clusters = model.run(parsedData)
    
    //Save where the points are located 
    val plot = ( args.length == 6 )
    
    if(plot){
        val ptsWithCluster = parsedData.map{ point => 
                            val prediction = clusters.predict(point)
                            (prediction, point.toString) }
        ptsWithCluster.coalesce(1).saveAsTextFile(args(3))
    
        //Save what the centers are
        sc.parallelize( clusters.clusterCenters ).coalesce(1).zipWithIndex.saveAsTextFile(args(4))
    }
    
    sc.stop()
  }
}
