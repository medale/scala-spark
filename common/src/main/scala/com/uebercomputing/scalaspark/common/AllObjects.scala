package com.uebercomputing.scalaspark.common

/**
  *
  */
object AllObjects {

  def main(args: Array[String]): Unit = {
    val area = 40 * 3
    val areaExplicit = 40.*(3)

    val hex = 15.toHexString
    println(hex)

    val fStatus = if (hex == "f") {
      "it was f"
    } else {
      "it wasn't f"
    }

    println(s"Hex's f-status was: ${fStatus}")
  }
}
