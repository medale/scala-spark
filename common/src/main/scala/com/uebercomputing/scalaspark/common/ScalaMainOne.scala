package com.uebercomputing.scalaspark.common

class ScalaMainOne(val answer: Int)
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
