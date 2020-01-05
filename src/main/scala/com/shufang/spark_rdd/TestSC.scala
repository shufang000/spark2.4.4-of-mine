package com.shufang.spark_rdd

import com.shufang.utils.SparkUtils

object TestSC {
  def main(args: Array[String]): Unit = {

    val sc = SparkUtils.getSC("local[*]","sparksc")

    println(sc.makeRDD(1 to 100).getNumPartitions)

    println(say("hello"))
    sc.stop()
  }


  def say(a:String):String={

    a +  "_world!"
  }



}
