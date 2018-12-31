package com.uebercomputing.scalaspark.analytics

import java.nio.file.Files
import java.nio.file.Paths

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

import scala.collection.JavaConverters._

/**
  * Must add Provided scope to run: Run - Edit Configurations... - HelloSparkWorld
  * use classpath of module - click Include Dependencies with Provided scope
  *
  * ~"static" class (object), main method,
  * expression-oriented (if construct returns value)
  * strings/triple quotes,
  * method definitions/return last line/Unit ~ void
  * SparkSession,SparkContext,RDD (like a distributed Scala collection)
  * map, flatMap, filter
  */
object HelloSparkWorld {

  val GhandiQuote =
    """Live as if you were to die tomorrow
      |Learn as if you were to live forever""".stripMargin

  val StopWords = List("be","to")

  def readLinesFromString(input: String): Seq[String] = {
    val lines = input.split("\n")
    lines
  }

  def readLinesFromFile(inputFile: String): Seq[String] = {
    val inputPath = Paths.get(inputFile)
    val linesJava: java.util.List[String] = Files.readAllLines(inputPath)
    val lines = linesJava.asScala //mutable.Buffer
    lines
  }

  def add(a: Int, b: Int): Int = a + b

  def process(spark: SparkSession, lines: Seq[String]): Unit = {
    val sc = spark.sparkContext

    val mixedCaseLinesRdd: RDD[String] = sc.parallelize(seq = lines, numSlices = 2)
    val lowerCaseLinesRdd = mixedCaseLinesRdd.map(line => line.toLowerCase)
    val wordsRdd = lowerCaseLinesRdd.flatMap(line => line.split("""\s+"""))
    val noStopWordsRdd = wordsRdd.filter(word => !StopWords.contains(word))

    val wordCountTuplesRdd = noStopWordsRdd.map { w => (w, 1) }

    // 1. explicitly declared def add(a: Int, b: Int): Int = a + b
    //    wordCountTuplesRdd.reduceByKey(add)
    // 2. explicit anonymous function
    //    wordCountTuplesRdd.reduceByKey((a: Int, b: Int) => a + b)
    // 3. anonymous function with syntactic sugar
    val wordCountsRdd = wordCountTuplesRdd.reduceByKey(_ + _)

    val localWordCounts = wordCountsRdd.collect()

    println(s"The word counts were: ${localWordCounts.mkString("\n","\n","\n")}")
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
      readLinesFromString(GhandiQuote)
    }

    val spark = SparkSession.builder.
      appName("HelloSparkWorld").
      master("local[2]").
      getOrCreate()

    process(spark, lines)

    spark.close()
  }
}
