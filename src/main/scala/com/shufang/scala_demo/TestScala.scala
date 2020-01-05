package com.shufang.scala_demo

object TestScala {
  def main(args: Array[String]): Unit = {

    val s = "aaaabbbaaaabbbbbbbbcccc"

    // 测试foldleft实现wordcount
    val charToInt: Map[Char, Int] = s.foldLeft(Map[Char, Int]()) {
      case (map, char) =>
        val amount = map.getOrElse(char, 0)
        map+(char -> (amount+1))
    }
    println(charToInt)
  }
}
