package kMeans;

import java.util.*;

class CanopyAlgorithm {

  static ArrayList<Points> findCenters(ArrayList<Points> dataPts, double tight, double loose){
    //Make a copy that we will manipulate
    ArrayList<Points> potentialPts = new ArrayList<>(dataPts);

    //Randomly Chosen centers will get added to this list
    ArrayList<Points> centers = new ArrayList<>();

    //These are the points that are within the loose distances
    Set<Points> loosePts = new HashSet<>();

    while(potentialPts.size() - loosePts.size() > 0){
      //Randomly select a point to make its own cluster
      int index = new Random().nextInt(potentialPts.size());
      Points center = potentialPts.get(index);
      centers.add(center);
      potentialPts.remove(center);
      loosePts.remove(center);

      for(int i = 0; i < potentialPts.size(); i++){
        double distance = calculateDistance(center.getCoords(), potentialPts.get(i).getCoords());

        if(distance <= tight)
          potentialPts.remove(i);

        else if(distance <= loose)
          loosePts.add(potentialPts.get(i));
      }
    }
    return centers;
  }

  static ArrayList<Points> canopyAlgorithm(int k, ArrayList<Points> dataPts, double tight,
      double loose){
    ArrayList<Points> centers = findCenters(dataPts, tight, loose);

    while(centers.size() != k){
      //Too many clusters -- run again on the centers only with larger bounds
      if(centers.size() > k){
        tight *= 1.5;
        loose *= 1.5;
        centers = findCenters(centers, tight, loose);
      }

      //Too few clusters -- run again with larger bounds
      else {
        tight *= .75;
        loose *= .75;
        centers = findCenters(dataPts, tight, loose);
      }
    }

    return centers;
  }

  private static double calculateDistance(double[] a, double[] b){
    double radicand = 0.0;
    for(int i = 0; i< a.length; i++)
      radicand += Math.pow( (a[i] - b[i]), 2);

    return Math.sqrt(radicand);
  }

}
