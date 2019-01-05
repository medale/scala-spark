package com.uebercomputing.scalaspark.common

object ImplicitRDDExplorer {

  def main(args: Array[String]): Unit = {
    val rdd: MyRDD[String] = new MyRDD("a")
    rdd.map((s:String) => s.length)
    //rdd does not have access to reduceByKey
    //T is String

    val tuple = new MyRDD((1,2))
    //tuple has access to implicit conversion to MyPairRDDFunctions
    //T is (Int,Int)
    tuple.reduceByKey(_ + _)
  }

}
