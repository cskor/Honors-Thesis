package linear;

import java.io.IOException;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Reducer;

public class LinearRegressionReducer extends
    Reducer<LongWritable, FloatWritable, LongWritable, FloatWritable> {

  public void reduce(LongWritable key, Iterable<LongWritable> values, Context context)
      throws IOException, InterruptedException {

    //The reducer just has to sum all the values for a given key
    float sum = 0;
    for(LongWritable val: values)
      sum += val.get();

    context.write(key, new FloatWritable(sum));

  }
}
