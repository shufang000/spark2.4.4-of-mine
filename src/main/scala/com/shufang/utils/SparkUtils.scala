package com.shufang.utils

import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.{Durations, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

object SparkUtils {

  //获取sc的方法
  def getSC(master: String, appname: String): SparkContext = {
    val conf = new SparkConf().setMaster(master).setAppName(appname)
    val sc = new SparkContext(conf)
    sc
  }
  def getSS(master: String, appname: String): SparkSession ={

    val spark = SparkSession.builder().appName(appname).master(master).getOrCreate()
    spark
  }

  def getSSC(master: String, appname: String,interval :Int): StreamingContext ={
    val ssc = new StreamingContext(getSC(master,appname),Durations.seconds(interval))
    ssc
  }
}
