package linear;

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.filecache.DistributedCache;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class LinearRegressionJob {
  public static void main(String[] args) {
    try {
      //Setup for the first job
      Configuration conf = new Configuration();

      //Setup for jar of class
      Job job = Job.getInstance(conf, "Linear Regression");
      job.setJarByClass(LinearRegressionJob.class);

      //Try to load the theta values into the distributed cache
      DistributedCache.addCacheFile(new Path(args[0]).toUri(), job.getConfiguration());

      job.setMapperClass(LinearRegressionMapper.class);
      job.setReducerClass(LinearRegressionReducer.class);
      job.setCombinerClass(LinearRegressionReducer.class);

      //Set the outputs
      job.setOutputKeyClass(LongWritable.class);
      job.setOutputValueClass(FloatWritable.class);

      //set the inputs
      job.setInputFormatClass(TextInputFormat.class);
      job.setOutputFormatClass(TextOutputFormat.class);

      // path to input in HDFS
      FileInputFormat.setInputPaths(job, new Path(args[1]));
      // path to output in HDFS
      FileOutputFormat.setOutputPath(job, new Path(args[2]));

      // Block until the job is completed.
      System.exit(job.waitForCompletion(true) ? 0 : 1);

    } catch (IOException | InterruptedException | ClassNotFoundException e) {
      System.err.println(e.getMessage());
    }

  }
}
