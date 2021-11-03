package class5

import scala.concurrent.Future

object Exercise1 {

  def method1: Either[String, Future[Int]] = ???
  def method2(i: Int): Either[String, Future[Char]] = ???

//  for {
//    val1 <- method1
//    val2 <- val1.map(i => method2(i))
//  }


  case class EitherTFuture[A](either: Either[String, Future[A]]) {

    def map[B](f: A => B): EitherTFuture[B] = ???
    def flatMap[B](f: A => EitherTFuture[B]): EitherTFuture[B] = ???

  }

}
