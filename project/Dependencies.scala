import sbt._

object Dependencies {

  val sparkVersion = "2.4.0"

  //match Spark's pom for these dependencies!
  val scalaVersionStr = "2.11.8"
  val hadoopVersion = "2.7.3"
  //end of Spark version match

  lazy val commonDependencies = Seq(
     ("org.scala-lang" % "scala-library" % scalaVersionStr),
     ("commons-io" % "commons-io" % "2.4")
  )

  //Avro, CSV - https://spark-packages.org/
  lazy val sparkDependenciesBase = Seq(
    ("org.apache.spark" %% "spark-core" % sparkVersion)
      .exclude("org.scalatest", "scalatest_2.11"),
    ("org.apache.spark" %% "spark-sql" % sparkVersion)
      .exclude("org.scalatest", "scalatest_2.11")
  )

  lazy val sparkDependencies = sparkDependenciesBase.map(_ % "provided")

  //test and integration test dependencies/scope
  lazy val testDependencies = Seq(
    ("org.scalatest" %% "scalatest" % "3.0.5" % "it,test")
  )

  lazy val sparkTestDependencies = Seq(
    ("com.holdenkarau" %% "spark-testing-base" % "2.3.1_0.10.0" % "it,test"),
    ("org.apache.spark" %% "spark-hive" % sparkVersion % "it,test")
  )
}
