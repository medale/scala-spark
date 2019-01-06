package com.uebercomputing.scalaspark.analytics

import org.apache.spark.sql.Dataset
import org.apache.spark.sql.Row
import org.apache.spark.sql.SparkSession

case class Person(firstName: String,
                  lastName: String,
                  age: Int)

class FullName(firstName: String, lastName: String)

/**
  * Object entry point - runnable main method (don't use class!)
  */
object HelloSparkDatasetWorld {

  def createPersonDataset(spark: SparkSession,
                          persons: Seq[Person]): Dataset[Person] = {
    //createDataset[T : Encoder](data: Seq[T]): Dataset[T]
    //implicit def newSequenceEncoder[T <: Seq[_] : TypeTag]: Encoder[T]

    //Or - via SQLImplicits
    //localSeqToDatasetHolder - DatasetHolder.toDS()
    //val people: Dataset[Person] = persons.toDS()

    //If not a Scala primitive or case class error:
    // Error: Unable to find encoder for type FullName.
    // An implicit Encoder[FullName] is needed to store FullName instances in a Dataset.
    // Primitive types (Int, String, etc) and Product types (case classes) are supported
    // by importing spark.implicits._  Support for serializing other types will be added
    // in future releases.
    //val names = List(new FullName("J","Doe"))
    //val ns = spark.createDataset(names)

    import spark.implicits._
    spark.createDataset(persons)
  }

  def countAgeLessThanCutoff(spark: SparkSession,
                             people: Dataset[Person],
                             ageCutoff: Int = 42): Long = {

    //for $
    import spark.implicits._

    //using: def where(condition: Column)
    //also: def where(conditionExpr: String)
    val youngers: Dataset[Row] = people.
      where($"age" < ageCutoff).
      select("firstName")

    //people.where($"age".<(ageCutoff))

    //column equality is === - the == is Scala equals
    //people.where($"firstName" === "Jane")

    youngers.count()
  }

  def createDatasetFromStrings(spark: SparkSession, inputs: Seq[String]): Dataset[String] = {
    import spark.implicits._
    spark.createDataset(inputs)
  }

  def processNames(spark: SparkSession,
                   namesDs: Dataset[String]): Long = {

    //need implicit Encoder for map
    import spark.implicits._
    val firsts = namesDs.map { fullName =>
      val firstLast = fullName.split(" ")
      firstLast(0)
    }

    println("\nAll first names:")
    firsts.show()

    firsts.count()
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

    //Person.apply("John...
    val persons = List(Person("John", "Doe", 42),
      Person("Jane", "Doe", 43))
    val people = createPersonDataset(spark, persons)
    countAgeLessThanCutoff(spark, people)

    val fullNames = List("John Doe", "Jane Doe")
    val namesDs = createDatasetFromStrings(spark, fullNames)
    processNames(spark, namesDs)

    spark.close()
  }
}
