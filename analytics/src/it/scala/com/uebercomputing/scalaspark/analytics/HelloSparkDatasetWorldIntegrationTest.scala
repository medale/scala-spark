package com.uebercomputing.scalaspark.analytics

import com.holdenkarau.spark.testing.DatasetSuiteBase
import org.scalatest.FunSpec
import org.scalatest.Matchers

/**
  *
  */
class HelloSparkDatasetWorldIntegrationTest extends FunSpec
  with Matchers with DatasetSuiteBase {

  val johnD = Person("John", "Doe", 42)
  val janeD = Person("Jane", "Doe", 43)
  val babyD = Person("Bob", "Doe", 1)

  val MyPersons = List(johnD, janeD, babyD)

  describe("countAgeLessThanCutoff") {
    it("should return count = all for high cutoff") {
      val ageCutoff = 99
      val people = HelloSparkDatasetWorld.
        createPersonDataset(spark, MyPersons)

      val expectedCount = MyPersons.size
      val actualCount = HelloSparkDatasetWorld.
        countAgeLessThanCutoff(spark, people, ageCutoff)

      assert(expectedCount === actualCount)
    }
  }
}
