package com.shufang.spark_sql

import com.shufang.utils.SparkUtils
import org.apache.spark.sql.DataFrame

object SaveSourceDemo {
  def main(args: Array[String]): Unit = {

    val spark = SparkUtils.getSS("local[*]", "savedemo")

    import spark.implicits._
    val emps = Array(("bobo", 18), ("lili", 32), ("aiai", 26))
    val df: DataFrame = emps.toList.toDF("name", "age")

    df.show()
    //    df.write.save()


    spark.stop()
  }
}
