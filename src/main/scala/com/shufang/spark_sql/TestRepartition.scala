package com.shufang.spark_sql

import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

object TestRepartition {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("partitions")

    val spark: SparkSession = SparkSession.builder().config(conf).getOrCreate()

    val df = spark.read.textFile("/Users/shufang/idea_project/spark244/src/main/testdata/helloworld.txt")

    // 打印df的schema结构
    df.printSchema()
    val sc = spark.sparkContext

    val rdd: RDD[String] = sc.textFile("src/main/testdata/helloworld.txt")
    val rdd2 = sc.parallelize(1 to 10)
    val rdd3 = sc.makeRDD(1 to 100)


    sc.stop()
    spark.stop()
  }

}
