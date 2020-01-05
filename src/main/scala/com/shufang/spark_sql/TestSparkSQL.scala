package com.shufang.spark_sql

import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}

object TestSparkSQL {

  def main(args: Array[String]): Unit = {

    // 配置conf
    //    val conf: SparkConf = new SparkConf().setAppName("sparksql").setMaster("local[*]")

    // 创建sparksession实例
    val spark: SparkSession = SparkSession.builder().appName("sparksql").master("local[*]").getOrCreate()


    // 从文件读取数据
    val ds: Dataset[String] = spark.read.textFile("src/main/testdata/helloworld.txt")

    // 导入隐式转换
    import spark.implicits._

    val ds1: Dataset[(String, Int)] = ds.flatMap(line => line.split(" ")).map((_, 1))

    ds1.cache()


    // 从MySQL数据源读取数据
    val frame: DataFrame = spark.read.format("jdbc")
      .option("driver", "com.mysql.jdbc.Driver")
      .option("url", "jdbc:mysql://localhost:3306/hello")
      .option("user", "root")
      .option("password", "000000")
      .option("dbtable", "a")
      .load()

    val ds2 = frame.as[People]

    ds2.show()


    val ds5 = ds2.mapPartitions {
      case peoples =>
        peoples.toArray.sortWith((p1, p2) => p1.id > p2.id).iterator
    }
    ds5.show()

    // 将ds写进MySQL
    /*ds1.write.format("jdbc")
      .option("driver", "com.mysql.jdbc.Driver")
      .option("url", "jdbc:mysql://localhost:3306/hello")
      .option("user", "root")
      .option("password", "000000")
      .option("dbtable", "helloworld20191127")
      .save()*/

    ds1.createTempView("hello")

    val df1 = spark.sql("select * from hello")


    // df 不是强类型的，缺少encode，但是有自己的schema，可以通过getInt getString来获取值
    val row: Row = df1.first()


    // df => rdd
    val rdd = df1.rdd
    //    rdd.map(row => row.getString(0)).foreach(println(_))

    val ds3: Dataset[Row] = df1.filter(row => row.getString(0).contains("hell"))

    val ds4: Dataset[People] = ds3.map(row => People(row.getInt(1), row.getString(0)))



    // 关闭 spark资源，spark是一种规划式的内存分配
    spark.stop()


  }

}


