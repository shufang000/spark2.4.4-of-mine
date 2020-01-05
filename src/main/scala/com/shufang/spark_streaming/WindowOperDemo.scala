package com.shufang.spark_streaming

import com.shufang.utils.SparkUtils
import org.apache.spark.streaming.Seconds
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}

object WindowOperDemo {
  def main(args: Array[String]): Unit = {
    val ssc = SparkUtils.getSSC("local[*]", "window", 3)

    val dst: ReceiverInputDStream[String] = ssc.socketTextStream("localhost", 9999)

    val dstkv: DStream[(String, Int)] = dst.map((_, 1))

    ssc.checkpoint("/Users/shufang/idea_project/spark244/src/main/testdata/checkpoint")
    /** *****************************************************************************************|
     * 方式一:                                                                                   |
     * dstkv.reduceByKeyAndWindow((a:Int,b:Int) =>{a+b},Seconds(6),Seconds(3)).print()          |
     *
     * ******************************************************************************************/

    // 方式二:
    dstkv.reduceByKeyAndWindow((a:Int,b:Int) => a+b,(a:Int,b:Int)=>a-b,Seconds(6),Seconds(3)).print()

    ssc.start()
    ssc.awaitTermination()

  }
}
