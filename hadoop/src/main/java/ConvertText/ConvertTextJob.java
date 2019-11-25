package ConvertText;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.mahout.math.VectorWritable;

public class ConvertTextJob {
  public static void main(String[] args){
    try {
      //Setup for the first job
      Configuration conf = new Configuration();

      //Setup for jar of class
      Job job = Job.getInstance(conf, "Convert Text");
      job.setJarByClass(ConvertTextJob.class);

      // path to input/output in HDFS
      FileInputFormat.addInputPath(job, new Path(args[0]));
      FileOutputFormat.setOutputPath(job, new Path(args[1]));

      //Set Mapper class
      job.setMapperClass(ConvertTextMapper.class);

      // Outputs from the Mapper
      job.setOutputKeyClass(NullWritable.class);
      job.setOutputValueClass(VectorWritable.class);

      //Set format of the key/value format
      job.setOutputFormatClass(SequenceFileOutputFormat.class);

      job.setNumReduceTasks(0);

      // Block until the job is completed.
      System.exit(job.waitForCompletion(true) ? 0 : 1);

    } catch (IOException | InterruptedException | ClassNotFoundException e) {
      System.err.println(e.getMessage());
    }
  }

}
