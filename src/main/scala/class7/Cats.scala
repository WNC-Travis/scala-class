package class7

import cats.{ Applicative, Monoid }
import cats.data.{ NonEmptyList, Validated }
import cats.data.Validated.{ Invalid, Valid }

object Cats extends App {

  val applicative: Applicative[Option] = new Applicative[Option] {
    override def pure[A](x: A): Option[A] = Some(x)

    override def ap[A, B](ff: Option[A => B])(fa: Option[A]): Option[B] = {
      fa.flatMap(a => {
        ff.flatMap(f => {
          Some(f.apply(a))
        })
      })
    }
  }

  val x = Some(1)
  val f = (x: Int) => x + 1

  applicative.map(x)(f)

  import cats.implicits._

  val i = Option(8) |+| Option(10) |+| Option(2)
  val n = NonEmptyList(1, List(2, 3, 4, 5))

  val isValid: Int => Validated[NonEmptyList[String], Int] = x =>
    if (x > 10) Valid(x) else Invalid("Must be at least 10").toValidatedNel

  val isEven: Int => Validated[NonEmptyList[String], Int] = x =>
    if (x % 2 == 0) Valid(x) else Invalid("Must be even").toValidatedNel

  val value1 = 8
  val value2 = 3

  val validationResult = (isValid(value1), isEven(value2)).mapN((v1, v2) => v1 + v2)

}
