package ConvertText;

import java.io.IOException;
import java.util.Vector;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
//import org.apache.mahout.math.VectorWritable;

public class ConvertTextMapper extends Mapper<LongWritable, Text, NullWritable, Text>{
  Vector<Double> clusters = new Vector<>();
  protected void map(LongWritable key, Text value, Context context)
      throws IOException, InterruptedException {
    clusters.clear();
    String[] clusterPts = value.toString().substring(1, value.toString().length() - 1).split(",");
    for(String point: clusterPts)
      clusters.add(Double.parseDouble(point));

    context.write(NullWritable.get(), new Text(" " + clusters.size()));

  }

}
