package com.uebercomputing.scalaspark.common

import scala.language.implicitConversions

/**
  *
  */
class MyRDD[T](v: T) {

  def map[U](func: T => U): MyRDD[U] = {
    new MyRDD(func(v))
  }
}

object MyRDD {
  implicit def rddToPairRDDFunctions[K,V](rdd: MyRDD[(K,V)]): MyPairRDDFunctions[K,V] = {
    new MyPairRDDFunctions(rdd)
  }
}