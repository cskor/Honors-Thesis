# Honors-Thesis
*Performance Profiling of Distributed Data Processing Frameworks to Inform Suitability for Analytic Jobs*

The amount of collected data has increased exponentially since devices have become more connected. On average, there are 500 million tweets sent, 5 billion online searches, 294 billion emails sent, and 4 petabytes of data created on Facebook every day. This has compelled companies to perform analytics on the collected data to improve business decisions that would generate higher profits and keep consumers satisfied. There are numerous tools that have their own way of loading and storing the data to perform analysis. With multiple options available for analytical engines, it is difficult to determine what engine is the most efficient in terms of time, memory usage, and resource management.

How does the choice of analytical engines impact completion times?  We will be looking at three popular engines: Hadoop, Spark, and Tensorflow. We will explore the performance effects for regression model fitting algorithms for the aforementioned engines. Different indicators such as number of page faults, throughput, completion times, and network I/O will be used to determine each engine’s impact on analytical performance.

This paper seeks to answer the question: given a dataset with certain types of characteristics, which engine would complete the task faster while effectively using computational resources?
