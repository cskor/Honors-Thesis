from __future__ import print_function

from pyspark.ml.clustering import KMeans
from pyspark.ml.evaluation import ClusteringEvaluator
from pyspark.ml.feature import VectorAssembler

from pyspark.sql import SparkSession

if __name__ == "__main__":
    spark = SparkSession\
        .builder\
        .appName("KMeansExample")\
        .getOrCreate()


    # Loads data.
    df = spark.read.csv("hdfs://nashville:30841/thesis/spark/*.csv", header=True)

    #Get the features
    features = df.columns
    
    #Take away the id
    features.remove("CUST_ID")
    
    #Convert all feature columns to floats
    for col in df.columns:
        if col in features:
            df = df.withColumn(col, df[col].cast('float'))

    #remove the null values
    df = df.na.drop()
    
    #Convert the dataframe into vector so kmeans can run on it
    vecAssembler = VectorAssembler(inputCols=features, outputCol="features")
    df_kmeans = vecAssembler.transform(df).select("CUST_ID", "features")

    # Trains a k-means model.
    kmeans = KMeans().setK(3).setFeaturesCol('features')
    model = kmeans.fit(df_kmeans)

    # Make predictions
    predictions = model.transform(df_kmeans)

    # Evaluate clustering by computing Silhouette score
    evaluator = ClusteringEvaluator()

    silhouette = evaluator.evaluate(predictions)
    print("Silhouette with squared euclidean distance = " + str(silhouette))

    # Shows the result.
    centers = model.clusterCenters()
    print("Cluster Centers: ")
    for center in centers:
        print(center)

    spark.stop()
