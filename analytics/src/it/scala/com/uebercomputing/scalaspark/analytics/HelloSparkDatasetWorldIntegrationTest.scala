package com.uebercomputing.scalaspark.analytics

import com.holdenkarau.spark.testing.DatasetSuiteBase
import org.scalatest.Assertion
import org.scalatest.FunSpec
import org.scalatest.Matchers

class HelloSparkDatasetWorldIntegrationTest extends FunSpec
  with Matchers with DatasetSuiteBase {

  val johnD = Person("John", "Doe", 42)
  val janeD = Person("Jane", "Doe", 43)
  val babyD = Person("Bob", "Doe", 2)

  val MyPersons = List(johnD, janeD, babyD)

  describe("countAgeLessThanCutoff") {

    def assertExpectedCountForCutoff(ageCutoff: Int,
                                     expectedCount: Int): Assertion = {
      val people = HelloSparkDatasetWorld.
        createPersonDataset(spark, MyPersons)

      val actualCount = HelloSparkDatasetWorld.
        countAgeLessThanCutoff(spark, people, ageCutoff)

      actualCount should equal (expectedCount)
    }

    it("should return count = all for high cutoff") {
      val ageCutoff = 99
      val expectedCount = MyPersons.size
      assertExpectedCountForCutoff(ageCutoff, expectedCount)
    }

    it("should return count = 0 for low cutoff") {
      val ageCutoff = 1
      val expectedCount = 0
      assertExpectedCountForCutoff(ageCutoff, expectedCount)
    }

    it("should return count = 2 for cutoff of 43") {
      val ageCutoff = 43
      val expectedCount = 2
      assertExpectedCountForCutoff(ageCutoff, expectedCount)
    }
  }
}
