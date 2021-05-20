package class1

import java.time.LocalDate

object Expressions extends App {

  // 1. Expressions
  // 2. Control Structures
  // 3. Option

  val x1: Int = 1 + 1

  val x2 = 1 + 1

  val x3 = 1L + 1

  val x4 = "Hello" + 1
  println(x4) // Hello1

  object World {
    override def toString = "World"
  }
  val x5 = "Hello " + World
  // Hello World
//  println(x5)
//
  val f1: String => Int = (str: String) => str.toInt
//  val f1a: Function1[String, Int] = ???

  def f2(str: String): Int = str.toInt

  val f3 = f2 _

  val e1 = throw new Exception()

//  // Control Statements
//
  val x6 = true
    if (true) "Hello"
    else "World"

  val pr = println("Hello World")

  val x7 = for (a <- 1 to 10) println(a+2)

  val x8 = for (a <- 1 to 10) yield a + 2
  x8.foreach(x => println(x))

  var x = 0
  val x9 = while (x < 10) x = x + 1

//  // Function Syntax

  def f4: String =
    if (true) "Hello"
    else "Word"

  def f5 =
    if (true) 1
    else 1L

  def f6 =
    if (true) 1
    else throw new Exception()

  def f8 =
    if (true) 1
    else LocalDate.now()

//  trait Base
  abstract class Base
  class Foo() extends Base
  class Bar() extends Base

  def f9 =
    if (true) new Foo()
    else new Bar()

  def f10 =
    if (true) new Foo()
    else "Bar"

  def f11: Option[String] =
    if (true) Some("Hello")
    else None

  // Option
//  trait Option[T]
//
//  object None extends Option[Nothing]
//  sealed class Some[T](t: T) extends Option[T]

  val opt: Option[String] = Some("Hello")
  val optInt: Option[Int] = opt.map[Int]( (str: String) => str.toInt)

  trait MyOption[T] {
    def map[A](f: T => A) : Option[A]
//    def flatMap[A](f: T => MyOption[A]): Option[A]
  }
//  None.get

//  val o1 = Some("Hello")
//  val o2 = Some("World")
//  val o3 = None

//  val hw = o1.map(h)













}