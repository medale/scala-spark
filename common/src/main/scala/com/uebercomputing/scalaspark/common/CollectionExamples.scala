package com.uebercomputing.scalaspark.common

/**
  *
  */
object CollectionExamples {

  def arrayExamples: Unit = {
    val myIntsExplicit : Array[Int] = Array.apply(1,3,7,9)

    val myInts = Array(1,3,7,9)

    //access
    val firstElementExplicit = myInts.apply(0)
    val firstElement = myInts(0)

    myInts(0) = 42

    val headElem = myInts.head
    val remainder = myInts.tail
    Some("b")
  }

  def main(args: Array[String]): Unit = {
    arrayExamples
  }
}
