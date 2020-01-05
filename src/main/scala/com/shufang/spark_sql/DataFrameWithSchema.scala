package com.shufang.spark_sql

import com.shufang.utils.SparkUtils
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.Row


object DataFrameWithSchema {
  def main(args: Array[String]): Unit = {

    val sc = SparkUtils.getSC("local[*]","schema")
    val spark = SparkUtils.getSS("local[*]","schema")

    // 定义结构字段
    val schemaString:String = "name age"

    // 千万别导错包,创建schema
    // StructField   StructType
    import org.apache.spark.sql.types._
    val fields = schemaString.split(" ")
      .map(fieldName => StructField(fieldName, StringType, nullable = true))
    val schema = StructType(fields)

    val rdd: RDD[(String, String)] = sc.textFile("/Users/shufang/idea_project/spark244/src/main/testdata/helloworld.txt").map(line => (line.split(" ")(0),line.split(" ")(1)))
    val rowRDD = rdd.map(a => Row(a._1,a._2))

    val df = spark.createDataFrame(rowRDD,schema)
    df.show()
    df.printSchema()


  }
}
