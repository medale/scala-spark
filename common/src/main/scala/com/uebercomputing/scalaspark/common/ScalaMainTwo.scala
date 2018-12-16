package com.uebercomputing.scalaspark.common


object ScalaMainTwo {

  def main(args: Array[String]): Unit = {
    println("Starting a Scala program...")
    //ScalaMainTwo.apply(42)
    val scMain = ScalaMainTwo(42)
    val answer = scMain.answer
    println(s"The answer was ${answer}")
  }

}

case class ScalaMainTwo(answer: Int)

