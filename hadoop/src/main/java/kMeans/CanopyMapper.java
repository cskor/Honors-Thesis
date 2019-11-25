package kMeans;

import java.io.IOException;
import java.util.ArrayList;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class CanopyMapper extends Mapper<LongWritable, Text, NullWritable, Text> {
  private ArrayList<Points> points = new ArrayList<>();

  protected void map(LongWritable key, Text value, Context context){
    //Input is # # .. # -- split on space
    String[] values = value.toString().split("\\s+");

    //Each line is a point so we are going to construct a point Object and save it to our array
    points.add(new Points(values));
  }

  protected void cleanup(Context context) throws IOException, InterruptedException {
    //Get the t1, t2, and k values
    Configuration conf = context.getConfiguration();
    double t1 = Double.parseDouble(conf.get("t1"));
    double t2 = Double.parseDouble(conf.get("t2"));
    int k = Integer.parseInt(conf.get("k"));

    ArrayList<Points> centers = CanopyAlgorithm.findCenters(points, t2, t1);
    for(Points center: centers){
      context.write(NullWritable.get(), new Text( center.toString() ));
    }
  }
}
