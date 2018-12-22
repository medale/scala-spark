package com.uebercomputing.scalaspark.analytics

import java.nio.file.Files
import java.nio.file.Paths

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

import scala.collection.JavaConverters._

/**
  * ~"static" class (object), main method,
  * expression-oriented (if construct returns value)
  * strings/triple quotes,
  * method definitions/return last line/Unit ~ void
  * SparkSession,SparkContext,RDD (like a distributed Scala collection)
  * map, flatMap, filter
  */
object HelloSparkWorld {

  val MarkTwainQuote =
    """Twenty years from now
      |you will be more disappointed
      |by the things that you didn't do
      |than by the ones you did do""".stripMargin

  val StopWords = List("be","the")

  def readLinesFromString(input: String): Seq[String] = {
    val lines = MarkTwainQuote.split("\n")
    lines
  }

  def readLinesFromFile(inputFile: String): Seq[String] = {
    val inputPath = Paths.get(inputFile)
    val linesJava: java.util.List[String] = Files.readAllLines(inputPath)
    val lines = linesJava.asScala //mutable.Buffer
    lines
  }

  def process(spark: SparkSession, lines: Seq[String]): Unit = {
    val sc = spark.sparkContext

    //val linesRdd = sc.parallelize(seq = lines, numSlices = 2)
    val mixedCaseLinesRdd: RDD[String] = sc.parallelize(seq = lines, numSlices = 2)
    val lowerCaseLinesRdd = mixedCaseLinesRdd.map(line => line.toLowerCase)
    val wordsRdd = lowerCaseLinesRdd.flatMap(line => line.split("""\s+"""))
    val noStopWordsRdd = wordsRdd.filter(word => !StopWords.contains(word))
    val localWords = noStopWordsRdd.collect()

    println(s"The words were: ${localWords.mkString("\n","\n","\n")}")
  }

  /**
    * Need to add "provided" scope back to runtime:
    * Run - Edit configurations - HelloSparkWorld -
    * use classpath of module - check box
    *
    * @param args
    */
  def main(args: Array[String]): Unit = {

    val lines = if (!args.isEmpty) {
      val inputFile = args(0)
      readLinesFromFile(inputFile)
    } else {
      readLinesFromString(MarkTwainQuote)
    }

    val spark = SparkSession.builder.
      appName("HelloSparkWorld").
      master("local[2]").
      getOrCreate()

    process(spark, lines)

    spark.close()
  }
}
