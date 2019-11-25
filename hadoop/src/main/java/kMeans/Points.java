package kMeans;

public class Points {
  private double[] coords;

  public Points(String[] values) {
    coords = new double[values.length];

    //Populate the coordinate array
    for(int i = 0; i < values.length; i++){
      coords[i] = Double.parseDouble(values[i]);
    }
  }

  public double[] getCoords() {
    return coords;
  }

  public String toString() {
    StringBuilder values = new StringBuilder();

    for (double pt : coords)
      values.append(" ").append(pt);

    return values.toString();
  }
}
