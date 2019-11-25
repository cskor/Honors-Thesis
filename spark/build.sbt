name := "Honors Thesis"

version := "1.0"

scalaVersion := "2.11.12"
val sparkVersion = "2.4.1"

libraryDependencies ++= Seq(
	"org.apache.spark" %% "spark-sql" % "2.3.0",
	"org.apache.spark" %% "spark-mllib" % "2.4.4"
)

