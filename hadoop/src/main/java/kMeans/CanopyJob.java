package kMeans;

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class CanopyJob {
  public static void main(String[] args) {
   try {
  //Setup for the first job
    Configuration conf = new Configuration();

    //Set the distances so reducer/mapper can read them
    conf.set("t1", args[2]);
    conf.set("t2", args[3]);
    conf.set("k", args[4]);

    //Setup for jar of class
    Job job = Job.getInstance(conf, "Canopy Algorithm");
    job.setJarByClass(CanopyJob.class);

    job.setMapperClass(CanopyMapper.class);
    job.setReducerClass(CanopyReducer.class);

    // Outputs from the Mapper.
    job.setMapOutputKeyClass(NullWritable.class);
    job.setMapOutputValueClass(Text.class);

    //Set the outputs
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(NullWritable.class);

    job.setNumReduceTasks(1);

    // path to input in HDFS
    FileInputFormat.addInputPath(job, new Path(args[0]));
    // path to output in HDFS
    FileOutputFormat.setOutputPath(job, new Path(args[1]));

    // Block until the job is completed.
    System.exit(job.waitForCompletion(true) ? 0 : 1);

  } catch (IOException | InterruptedException | ClassNotFoundException e) {
    System.err.println(e.getMessage());
  }

  }
}
