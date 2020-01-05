package com.shufang.spark_rdd

import com.shufang.utils.SparkUtils
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.rdd.RDD

object TestBroadcaseV {
  def main(args: Array[String]): Unit = {
    val sc = SparkUtils.getSC("local[*]", "broadcast")

    val rdd1: RDD[Int] = sc.makeRDD(1 to 10)


    /** **************************************｜
     * Accumulator 和 Broadcast都是只读的      ｜
     * ***************************************/
    //    val i:Int = 10
    //    val bc: Broadcast[Int] = sc.broadcast(10)
    val acc = sc.longAccumulator("acc累加器")

    println(acc.isZero)
    val rdd2 = rdd1.map {
      a =>
        acc.add(1)
        a + 1
    }

    // 累加器也是延迟执行的，所以需要一个执行算子触发job
    rdd2.collect()

    //获取累加器最终的值
    println(acc.isZero)
    println(acc.name + ": " + acc.value.toString)

    sc.stop()
  }
}
