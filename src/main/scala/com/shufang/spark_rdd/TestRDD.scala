package com.shufang.spark_rdd

import java.util.concurrent.atomic.LongAccumulator

import com.shufang.utils.SparkUtils
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Dataset}
import org.apache.spark.util.{LongAccumulator, SizeEstimator}
import org.apache.spark.{SparkConf, SparkContext}

/** **************************************************************************
 * spark.defaultParallelizes = max(executors的总核数，2)         ->total—cores｜
 * spark.defaultMaxPartitionSize = 128M                                     ｜
 * ｜
 * sc.defaultParalleizes = spark.defaultParallelizes                        ｜
 * sc.defaultMinPartitions = min(sc.defaultParallelizes , 2)   ->2          ｜
 * ***************************************************************************/

object TestRDD {

  def main(args: Array[String]): Unit = {

    val sc = SparkUtils.getSC("local[*]", "testRDD")

    //可以指定minPartitions，这个Partitions就是分区数,
    //如果不指定,默认 partitions = max(文件切片数,sc.defaultMinPartitions)
    val rdd: RDD[String] = sc.textFile("/Users/shufang/idea_project/spark244/src/main/testdata/helloworld.txt", 5)
    println("rdd的分区数为：" + rdd.getNumPartitions)

    //如果指定了numSlices，分区数就是numSlices
    val rdd1 = sc.makeRDD(1 to 10, 5)
    println("rdd1的分区数为：" + rdd1.getNumPartitions)

    // 求字节最长的一行
    val str: String = rdd.reduce((a, b) => if (a.length > b.length) a else b)
    println(str)

    // sortby是一个转换算子
    val rdd3: RDD[String] = rdd.sortBy(_.length, false)
    println(rdd3.take(2).mkString("->>"))


    //val value: RDD[(String, Iterable[(String, Int)])] = rdd.flatMap(_.split(" ")).map((_,1)).groupBy(k=>k._1)

    val rdd4: RDD[(String, Int)] = rdd.flatMap(_.split(" ")).map((_,1)).reduceByKey(_+_)

    println(rdd4.collect().mkString("\n"))

    // 估算RDD占用的内存大小
    val l: Long = SizeEstimator.estimate(rdd)
    println(l)


    val spark = SparkUtils.getSS("local[*]","session")
    import spark.implicits._

    val df: DataFrame = rdd4.toDF("key","values")

    //
    df.printSchema()
    val ds: Dataset[(String, Int)] = df.map(kv => (kv.getAs[String](0),kv.getAs[Int](1)))

    ds.show()


    spark.stop()
    sc.stop()
  }
}
