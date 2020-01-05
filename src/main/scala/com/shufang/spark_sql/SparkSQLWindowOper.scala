package com.shufang.spark_sql

import com.shufang.utils.SparkUtils
import org.apache.spark.sql.{DataFrame, Dataset}
import org.apache.spark.sql.expressions.WindowSpec

object SparkSQLWindowOper {
  def main(args: Array[String]): Unit = {
    val spark = SparkUtils.getSS("local[*]", "sql")



    val ds: Dataset[String] = spark.read.textFile("/Users/shufang/idea_project/spark244/src/main/testdata/helloworld.txt")


    import spark.implicits._
    val ds1 = ds.flatMap(_.split(" ")).map((_,1))
    ds1.show(23)

    import org.apache.spark.sql.expressions.Window
    import org.apache.spark.sql.functions._

    ds1.cache()
    val df = ds1.toDF("name","count")

    val spec: WindowSpec = Window.partitionBy("name").orderBy("name").rowsBetween(-1,1)
    val df1: DataFrame = df.withColumn("new_col",sum(df("count")).over(spec))

    df1.show()

    spark.stop()
  }
}
