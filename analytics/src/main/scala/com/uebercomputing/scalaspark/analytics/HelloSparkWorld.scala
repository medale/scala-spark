package com.uebercomputing.scalaspark.analytics

import java.nio.file.Files
import java.nio.file.Paths
import java.util.{List => JavaList}

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
    val linesJava: JavaList[String] = Files.readAllLines(inputPath)
    val lines = linesJava.asScala //mutable.Buffer
    lines
  }

  def wordCountLocal(lines: Seq[String]): Unit = {

    def toLower(s: String): String = {
      s.toLowerCase
    }
    //explicitly call function
    val lowerLines = lines.map(toLower)

    val words = lowerLines.flatMap { line =>
      line.split("""\s+""")}

    val noStopWords = words.filter(!StopWords.contains(_))

    val wordsMap: Map[String,Seq[String]] =
      noStopWords.groupBy( w => identity(w))
    //wordsMap.mapValues(vs => vs.size)
    val wordCountsMap = wordsMap.mapValues(_.size)
    val countsString = wordCountsMap.mkString("\n","\n","\n")
    println(s"The word counts were: ${countsString}")
  }

  def wordCountRdd(spark: SparkSession, lines: Seq[String]): Unit = {
    //for real processing -
    //val mixedLinesRdd = spark.read.textFile(inputPath).rdd
    val sc = spark.sparkContext

    val mixedLinesRdd: RDD[String] = sc.parallelize(seq = lines, numSlices = 2)
    val lowerLinesRdd = mixedLinesRdd.map(_.toLowerCase)
    val wordsRdd = lowerLinesRdd.flatMap(_.split("""\s+"""))
    val noStopWordsRdd = wordsRdd.filter(!StopWords.contains(_))

    //Don't use groupBy - very expensive to shuffle words across partition!

    val wordCountTuplesRdd = noStopWordsRdd.map { (_, 1) }
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

    wordCountLocal(lines)

    val spark = SparkSession.builder.
      appName("HelloSparkWorld").
      master("local[2]").
      getOrCreate()

    wordCountRdd(spark, lines)

    spark.close()
  }
}
