package com.uebercomputing.scalaspark.analytics

import org.apache.spark.sql.Dataset
import org.apache.spark.sql.Row
import org.apache.spark.sql.SparkSession

case class Person(firstName: String, lastName: String, age: Int)

/**
  * Object entry point - runnable main method (don't use class!)
  */
object HelloSparkDatasetWorld {

  def process(spark: SparkSession): Unit = {

    //persons: List[Person]
    val persons = List(Person("John","Doe",42),
      Person("Jane","Doe",43))

    println(persons(0).isInstanceOf[Product])

    val people: Dataset[Row] = spark.createDataFrame(persons)

    val uniqueLastNames: Dataset[Row] = people.select("lastName").distinct()

    val uniqueNamesLocal: Array[Row] = uniqueLastNames.collect()

    println(s"The unique last names were: ${uniqueNamesLocal.mkString(",")}")
  }

  /**
    * Need to add "provided" scope back to runtime:
    * Run - Edit configurations - HelloSparkDatasetWorld - use classpath of module - check box
    *
    * @param args
    */
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().
      appName("HelloSparkDatasetWorld").
      master("local[2]").
      getOrCreate()
    process(spark)
  }
}
