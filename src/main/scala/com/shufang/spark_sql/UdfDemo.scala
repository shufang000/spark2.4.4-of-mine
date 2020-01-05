package com.shufang.spark_sql

import org.apache.spark.sql.expressions.{MutableAggregationBuffer, UserDefinedAggregateFunction, UserDefinedFunction}
import org.apache.spark.sql.types.{DataType, StructType}
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}

object UdfDemo {
  def main(args: Array[String]): Unit = {
    // 创建sparksession实例
    val spark: SparkSession = SparkSession.builder().appName("sparksql").master("local[*]").getOrCreate()

    // 从文件读取数据
    val ds: Dataset[String] = spark.read.textFile("src/main/testdata/helloworld.txt")
    val df = ds.toDF("name")


    //    val up: UserDefinedFunction = spark.udf.register("up",toUpper(_))


    df.show()


    /**
     * 方式一：通过匿名函数的形式创建自定义函数
      +--------+
      |    name|
      +--------+
      |zhangsan|
      |    lisi|
      |  wangwu|
      | zhaoliu|
      |zhangsan|
      +--------+

      +--------+-------------+
      |    name|UDF:upp(name)|
      +--------+-------------+
      |zhangsan|     ZHANGSAN|
      |    lisi|         LISI|
      |  wangwu|       WANGWU|
      | zhaoliu|      ZHAOLIU|
      |zhangsan|     ZHANGSAN|
      +--------+-------------+
     */
    val upp = spark.udf.register("upp", (s: String) => s.toUpperCase)
    val df1: DataFrame = df.select(df("name"),upp(df.col("name")) )
    df1.show()


    /**
     * 方式二： 通过实名函数的形式创建UDF：UserDefinedFunction
     * @param s
     * @return
     * name_len1(df.col("name")) as "length" 用自定义函数后可以通过as 来取别名，很方便
     *
    +--------+------------------+
    |    name|UDF:name_len(name)|
    +--------+------------------+
    |zhangsan|                 8|
    |    lisi|                 4|
    |  wangwu|                 6|
    | zhaoliu|                 7|
    |zhangsan|                 8|
    +--------+------------------+
     */
    def name_len(s:String):Int={
      s.length
    }

    val name_len1: UserDefinedFunction = spark.udf.register("name_len",name_len(_))
    //  1.在df的算子函数中使用
    df.select(df.col("name"),name_len1(df.col("name")) as "length" ).show()
    //  2.在sparksql中的sql中使用，这个函数名与`1`不同
    spark.sql("Select name,name_len(name) from names").show()

    /**
     * 方式三：DataFrame的udf方法
     * 但是这个自定义函数不能用在spark.sql中，因为不能给方法取别名
     */
    import  org.apache.spark.sql.functions._
    val name_lenth: UserDefinedFunction = udf((name:String) => name.length)

    df.createOrReplaceTempView("names")
    val df2 = df.select(name_lenth(df("name")) as "length")
    df2.show()



    spark.stop()
  }
}

class MyUDAF extends UserDefinedAggregateFunction{
  override def inputSchema: StructType = ???

  override def bufferSchema: StructType = ???

  override def dataType: DataType = ???

  override def deterministic: Boolean = ???

  override def initialize(buffer: MutableAggregationBuffer): Unit = ???

  override def update(buffer: MutableAggregationBuffer, input: Row): Unit = ???

  override def merge(buffer1: MutableAggregationBuffer, buffer2: Row): Unit = ???

  override def evaluate(buffer: Row): Any = ???
}
