package advent

import scala.io.Source

object Puzzle1 extends App {

  case class Input(last: Int, count: Int)
  val lines = Source.fromFile("/Users/travis.stevens/workspace/scala-class/src/main/resources/advent/puzzle1.txt")
    .getLines()
    .toList
    .map(_.toInt)

  println(s"Values: ${lines.length}")
  val head = lines.head
  val input = lines.drop(1)
    .foldLeft(Input(head,0)){ case (input, next) => {
      val nextCount = input.count +
        (if (next > input.last) 1 else 0)
      Input(next, nextCount)
    }}

  println(input.count)



}

object Puzzle1b extends App {

  val lines = Source.fromFile("/Users/travis.stevens/workspace/scala-class/src/main/resources/advent/puzzle1b.txt")
    .getLines()
    .toList
    .map(_.toInt)

  println(s"LINES: ${lines.length}")
  case class Input(r1: Int, r2: Int, r3: Int, count: Int, totalProcessed: Int)
  val initialInput = Input(lines.take(3).sum, lines.drop(1).take(2).sum, lines.drop(2).take(1).sum, 0, 3)
  println(initialInput)
  val result = lines.drop(3).foldLeft(initialInput){ case (input, next) =>
    val r2 = input.r2 + next
    val nextCount = input.count + (if (r2 > input.r1) 1 else 0)
    val nextInput = Input(r2, input.r3 + next, next, nextCount, input.totalProcessed + 1)
    println(nextInput + "|" + next)
    nextInput
  }

  println(result)


}


object Parameter {
  def printString(strSeq: String *): Unit = {
    strSeq.foreach(println)
  }

  printString("string1", "string2")

  val seq = Seq("String1", "string2")
  printString(seq :_*)




}
