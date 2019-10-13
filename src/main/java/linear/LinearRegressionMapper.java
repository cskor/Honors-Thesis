package linear;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.filecache.DistributedCache;

public class LinearRegressionMapper extends Mapper<LongWritable, Text, LongWritable, FloatWritable> {
    private Path[] localFiles;
    private FileInputStream fis = null;
    BufferedInputStream bis = null;

    public void setup(Context context)
    {
      //Read the distributed cache
      try {
        localFiles = DistributedCache.getLocalCacheFiles(context.getConfiguration());
      } catch (IOException e) { e.printStackTrace(); }
    }

    public void map(LongWritable key, Text value, Context context)
        throws IOException, InterruptedException {

      /**
       *
       * Linear-Regression costs function
       *
       * This will simply sum over the subset and calculate the predicted value
       * y_predict(x) for the given features values and the current theta values
       * Then it will subtract the true y values from the y_predict(x) value for
       * every input record in the subset
       *
       * J(theta) = sum((y_predict(x)-y)^2)
       * y_predict(x) = theta(0)*x(0) + .... + theta(i)*x(i)
       *
       */

    String line = value.toString();
    String[] features = line.split(",");

    //calculate the costs
    context.write(new LongWritable(1), new FloatWritable(costs(features)));
  }

    private float costs(String[] values)
    {
      //load the cached file
      File file = new File(localFiles[0].toString());
      float costs = 0;

      try {
        fis = new FileInputStream(file);
        bis = new BufferedInputStream(fis);

        BufferedReader d = new BufferedReader(new InputStreamReader(bis));
        String line = d.readLine();

      //all right we have all the theta values, lets convert them to floats
        String[] theta = line.split(",");


        //first value is the y value
        float y = Float.valueOf(values[0]);

        //Calculate the costs for each record in values
        for(int j = 1; j < values.length; j++)
        {
          //bias calculation
          if(j == 1)
            costs += (new Float(theta[j]))*1;
          else {
            costs += Float.valueOf(theta[j])  * Float.valueOf(values[0]);
          }

        }

        // Subtract y and square the costs
        costs = (costs -y)*(costs - y);

      } catch (FileNotFoundException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      }

      return costs;

    }
}
