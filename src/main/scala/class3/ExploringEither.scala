package class3

import scala.language.higherKinds

object ExploringEither {

  /*

trait Either[L,R]
case class Left[L](l: L) extends Either[L,Nothing]
case class Right[R](r: R) extends Either[Nothing,R]
 */

  val e1 = Left("Hello")
  val e2 = Right(42)
  val e3 = if (true) Left("Hello") else Right(42)

  e2.left.get //Fails with Right
  e1.right.get //Fails with Left

  val e4 = e3.map(i => i + 1)
  val e5 = e3.left.map(str => str + " World")

  // Either used as a Sum type

  case class Unemployed(months: Int)
  case class Hourly(hourlyPay: Int)
  case class Salary(salary: Int)
  case class Employed(title: String, companyName: String, salary: Either[Hourly, Salary])
  case class Person(name: String, job: Either[Unemployed, Employed])

  val p1 = Person("John", Right(Employed("Engineer", "Vestwell", Right(Salary(50000)))))
  val p2 = Person("Bob", Right(Employed("Line CooK", "Pops Diner", Left(Hourly(15)))))

  // map
  p1.job.map(employed => employed.companyName)
  p1.job.left.map(unemployed => unemployed.months)


  // flatmap
  p1.job.flatMap(employed => Right(employed.companyName))


  // Either used as error handling

  trait Error
  case class ValidationError(invalidFields: List[String]) extends Error
  case class DbError(message: String) extends Error

  case class Employer(name: String, offers401k: Boolean)

  def getPerson(id: Int): Either[Error, Person] = ???
  def getEmployer(name: String): Either[Error, Employer] = ???

  val personHas401k = for {
    person <- getPerson(1)
    employer <- getEmployer(person.name)
  } yield (person.name, employer.offers401k)

  getPerson(1)
    .flatMap((person: Person) =>
      ExploringEither.getEmployer(person.name)
        .map((employer: Employer) => Tuple2.apply(person.name, employer.offers401k))
    )

  val result = personHas401k match {
    case Left(err) => s"Error: ${err}"
    case Right(person401) if person401._2 => s"${person401._1} has a 401k"
    case Right(person401) =>   s"${person401._1} does not have a 401k"
  }
  println(result)


  personHas401k.fold(_.toString, _.toString)
  personHas401k.map(_.toString).merge
  val opt = personHas401k.toOption
  val r1 = opt.toRight("Left")
  val r2 = opt.toLeft("Right")



//  def getPerson2(id: Int): Either[Error, Person] = ???
//  def getEmployer2(name: String): Option[Employer] = ???
//
//  val personHas401k = for {
//    person <- getPerson2(1)
//    employer <- getEmployer2(person.name).toRight(DbError(s"Could not find employer by name: ${person.name}"))
//  } yield (person.name, employer.offers401k)




  def getPerson2(id: Int): Either[Error, Option[Person]] = ???
  def getEmployer2(name: String): Either[Error, Option[Employer]] = ???


  val result = for {
    maybePerson <- getPerson2(1)
    maybeEmployer <- maybePerson.map(person => getEmployer2(person.name)) match {
      case None => Right(None)
      case Some(either) => either
    }
  } yield {
    for {
      person <- maybePerson
      employer <- maybeEmployer
    } yield (person, employer)
  }


  case class OptionT[A](v: Either[Error, Option[A]]) {

    def map[B](f: A => B): OptionT[B] = {
      v match {
        case Left(err) => OptionT(Left(err))
        case Right(opt) => opt match {
          case None => OptionT(Right(None))
          case Some(a) => OptionT(Right(Some(f(a))))
        }
      }
    }

    def flatMap[B](f:  A => OptionT[B]) = {
      v match {
        case Left(err) => OptionT(Left(err))
        case Right(opt) => opt match {
          case None => OptionT(Right(None))
          case Some(a) => f(a)
        }
      }
    }

  }












}
