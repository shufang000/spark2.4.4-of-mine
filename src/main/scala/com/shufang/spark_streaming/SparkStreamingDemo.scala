package com.shufang.spark_streaming

import com.shufang.utils.SparkUtils
import org.apache.spark.rdd.RDD
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.{Durations, Seconds, StreamingContext}

import scala.collection.mutable

object SparkStreamingDemo {

  def main(args: Array[String]): Unit = {

    // 这里的2是将2秒接受的数据形成一个RDD
    val ssc: StreamingContext = SparkUtils.getSSC("local[*]", "streaming", 2)

    ssc.checkpoint("/Users/shufang/idea_project/spark244/src/main/testdata")
    val dst = ssc.socketTextStream("localhost", 9999, StorageLevel.MEMORY_ONLY)

    // val dst1: DStream[(String, Int)] = dst.map((_,1))
    //      .reduceByKeyAndWindow((a,b)=> a+b,Durations.seconds(10),Durations.seconds(5))

    // val dst1 = dst.window(Durations.seconds(10),Durations.seconds(5)).map((_,1)).reduceByKey(_+_)

    // 无状态的转换，每次只计算2s批次内的数据
    val dst1: DStream[(String, Int)] = dst.map((_, 1)).reduceByKey(_ + _)
    // dst1.print()

    // dst.transform(rdd => rdd)

    val dst2: DStream[Long] = dst1.count()

    //开始计算
    ssc.start()

    //等待计算结束
    ssc.awaitTermination()
  }
}
