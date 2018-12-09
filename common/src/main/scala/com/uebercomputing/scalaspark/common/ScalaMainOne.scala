package com.uebercomputing.scalaspark.common

/**
  * Starting a program from Scala.
  */
object ScalaMainOne {

  def main(args: Array[String]): Unit = {
    println("Starting a Scala program...")
    val scMain = new ScalaMainOne(42)
    val answer = scMain.answer
    println(s"The answer was ${answer}")
  }
}

class ScalaMainOne(val answer: Int) {

}
