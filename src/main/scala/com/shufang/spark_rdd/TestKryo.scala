package com.shufang.spark_rdd

import com.esotericsoftware.kryo.Kryo
import org.apache.spark.rdd.RDD
import org.apache.spark.serializer.KryoRegistrator
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable.ArrayBuffer
import scala.util.Random

/**
 * kryo序列化
 * 1.可以在spark-default.conf指定 -> spark.serializer        org.apache.spark.serializer.KryoSerializer
 * 2.可以通过命令行spark-submit --conf "spark.serializer" = "org.apache.spark.serializer.KryoSerializer"指定
 * 3.可以在代码中通过
 * new SparkConf()
 * .set("spark.serializer","org.apache.spark.serializer.KryoSerializer")指定
 * .set("spark.kryo.registrationRequired ","true")
 * 如果需要序列化的对象很大、需要指定 spark.kryoserializer.buffer  = 64K或者更大一些
 * classOf[scala.collection.mutable.WrappedArray.ofRef[_]]
 * 优先级 ： 2 > 3 > 1
 *
 * 【需要参考详细文档的话，请上spark的官网！TUNING 调优文档】
 */
object TestKryo {
  def main(args: Array[String]): Unit = {


    /**
     * spark.kryo.registrator
     */
    val conf: SparkConf = new SparkConf()
      .setMaster("local[*]")
      .setAppName("kryo")
      .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      .set("spark.kryo.registrationRequired ", "true")
//      .set("spark.kryo.registrator","com.shufang.spark_rdd.MyKryoRegist") //这样也能注册需要序列化的类
      .registerKryoClasses(Array(classOf[People], classOf[scala.collection.mutable.WrappedArray.ofRef[_]]))


    val sc = new SparkContext(conf)
    val peoples = ArrayBuffer[People]()

    val names = Array[String]("zhangsan", "lisi", "wangwu")
    val adds = Array[String]("shanghai", "beijing", "shenzhen", "guangzhou", "hangzhou")

    for (i <- 1 to 100000) {
      val people = new People
      people.id = Random.nextInt(100000)
      people.name = names(Random.nextInt(3))
      people.add = adds(Random.nextInt(5))

      peoples += people
//      peoples.append(people)
    }


    val prdd = sc.parallelize(peoples, 6)

    println(prdd.collect().mkString("\n"))


    sc.stop()


  }
}

class People {
  var id: Int = _
  var name: String = _
  var add: String = _

  override def toString: String =
    "People["+id + "  "+ name + "  "+ add+ "]" +
      ""
}


/*
  方式四
 */
class MyKryoRegist extends KryoRegistrator{
  override def registerClasses(kryo: Kryo): Unit = {
    kryo.register(classOf[People])
  }
}