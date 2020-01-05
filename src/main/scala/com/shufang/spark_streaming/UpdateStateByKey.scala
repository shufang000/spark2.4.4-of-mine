package com.shufang.spark_streaming

import com.shufang.utils.SparkUtils
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}

object UpdateStateByKey {
  def main(args: Array[String]): Unit = {

    val ssc = SparkUtils.getSSC("local[*]","streaming2",3)

    val dst: ReceiverInputDStream[String] = ssc.socketTextStream("localhost",9999)


    val dstkv: DStream[(String, Int)] = dst.map((_,1))


    /**
     * updateStateByKey(),全局持续有状态的转换
     */
    // updateStateByKey必须开启checkpoint
    ssc.checkpoint("/Users/shufang/idea_project/spark244/src/main/testdata/checkpoint")

    val updateFun = (values:Seq[Int],state:Option[Int]) => {
      val newstate: Int = values.sum
      val oldstate = state.getOrElse(0)
      Some(newstate+oldstate)
    }

    val value: DStream[(String, Int)] = dstkv.updateStateByKey(updateFun,5)

    value.print()
    ssc.start()
    ssc.awaitTermination()
  }
}
