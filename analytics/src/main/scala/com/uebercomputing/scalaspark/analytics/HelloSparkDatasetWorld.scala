package com.uebercomputing.scalaspark.analytics

import org.apache.spark.sql.Dataset
import org.apache.spark.sql.Row
import org.apache.spark.sql.SparkSession

case class Person(firstName: String,
                  lastName: String,
                  age: Int)

/**
  * Object entry point - runnable main method (don't use class!)
  */
object HelloSparkDatasetWorld {

  def processPerson(spark: SparkSession): Unit = {

    val persons = List(Person("John","Doe",42),
      Person("Jane","Doe",43))

    println(persons(0).isInstanceOf[Product])
    println(persons(0).isInstanceOf[Serializable])

    val people: Dataset[Row] = spark.createDataFrame(persons)

    people.printSchema()

    val uniqueLastNames: Dataset[Row] = people.select("lastName").distinct()

    uniqueLastNames.printSchema()

    val uniqueNamesLocal: Array[Row] = uniqueLastNames.collect()

    val uniqueNames = uniqueNamesLocal.map(r => r.getString(0))
    println(s"The unique last names were: ${uniqueNames.mkString(",")}")
  }

  def processString(spark: SparkSession): Unit = {
    val names = List("John Doe", "Jane Doe")

    import spark.implicits._
    val people: Dataset[String] = spark.createDataset(names)

    val firsts = people.map { fullName =>
      val firstLast = fullName.split(" ")
      firstLast(0)
    }

    firsts.show()
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

    processPerson(spark)
    processString(spark)

    spark.close()
  }
}
