package com.shufang.spark_streaming

import com.shufang.utils.SparkUtils
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}

object SparkStreamingDemo1 {
  def main(args: Array[String]): Unit = {
    val ssc = SparkUtils.getSSC("local[*]", "dst", 4)
    val dst: ReceiverInputDStream[String] = ssc.socketTextStream("localhost", 9999, StorageLevel.MEMORY_ONLY)

    //    dst.print()
    val value: DStream[(String, Int)] = dst.map((_, 1)).reduceByKey(_ + _)
    val value1: DStream[(String, Int)] = dst.map((_, 2)).reduceByKey(_ + _)
    //    value.print()

    val values: DStream[(String, (Iterable[Int], Iterable[Int]))] = value.cogroup(value1)

    values.print()
    ssc.start()
    ssc.awaitTermination()

    // value.updateStateByKey()
    // 广播变量广播RDD
    // 累加器
  }

}
