package com.uebercomputing.scalaspark.analytics

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

/**
  * ~"static" class (object), main method,
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

  def mkLines(input: String): Array[String] = {
    val lines = MarkTwainQuote.split("\n")
    lines
  }

  def process(spark: SparkSession): Unit = {
    val lines = mkLines(MarkTwainQuote)
    val sc = spark.sparkContext

    //val linesRdd = sc.parallelize(seq = lines, numSlices = 2)
    val mixedCaseLinesRdd: RDD[String] = sc.parallelize(seq = lines, numSlices = 2)
    val lowerCaseLinesRdd = mixedCaseLinesRdd.map(line => line.toLowerCase)

    println(s"We had ${lowerCaseLinesRdd.count} lines in our RDD")

    val wordsRdd = lowerCaseLinesRdd.flatMap(line => line.split("""\s+"""))

    println(s"We had ${wordsRdd.count} total words in our lines")

    val noStopWordsRdd = wordsRdd.filter(word => !StopWords.contains(word))

    println(s"We had ${noStopWordsRdd.count} words that weren't stop words in our lines")

    val localWords = noStopWordsRdd.collect()

    println(s"The words were: ${localWords.mkString("\n","\n","\n")}")

  }

  /**
    * Need to add "provided" scope back to runtime:
    * Run - Edit configurations - HelloSparkWorld - use classpath of module - check box
    *
    * @param args
    */
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().
      appName("HelloSparkWorld").
      master("local[2]").
      getOrCreate()

    process(spark)

    spark.close()
  }
}
