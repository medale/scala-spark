package com.uebercomputing.scalaspark.common

class MyPairRDDFunctions[K,V](self: MyRDD[(K, V)]) {

  def reduceByKey(func: (V, V) => V): MyRDD[(K, V)] = {
    self
  }
}
