package kMeans;

import java.io.IOException;
import java.util.ArrayList;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class CanopyReducer extends Reducer<NullWritable, Text, Text, NullWritable> {
  private ArrayList<Points> points = new ArrayList<>();

  protected void reduce(NullWritable key, Iterable<Text> values, Context context)
      throws IOException, InterruptedException {
    int size = 0;
    for( Text value: values) {
      size += 1;
      String[] point = value.toString().substring(1).split("\\s+");

      points.add(new Points(point));
    }

    context.write(new Text( "Size: " + size), NullWritable.get());

    Configuration conf = context.getConfiguration();
    double t1 = Double.parseDouble(conf.get("t1"));
    double t2 = Double.parseDouble(conf.get("t2"));
    int k = Integer.parseInt(conf.get("k"));

    ArrayList<Points> centers = CanopyAlgorithm.findCenters(points, t2, t1);
    for(Points center: centers){
      context.write( new Text( center.toString() ), NullWritable.get());
    }
  }
}
