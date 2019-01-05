package com.uebercomputing.scalaspark.common

/**
  *
  */
class MyRDD[T](v: T) {

  def map[T,U](func: T => U): MyRDD[U] = {
    new MyRDD(func(v))
  }
}

object MyRDD {
  implicit def rddToPairRDDFunctions[K,V](rdd: MyRDD[(K,V)]): MyPairRDDFunctions[K,V] = {
    new MyPairRDDFunctions(rdd)
  }
}