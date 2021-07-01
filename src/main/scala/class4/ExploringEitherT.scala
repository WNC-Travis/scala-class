package class4

import class3.ExploringEither.Error

import scala.concurrent.Future

object ExploringEitherT {

  trait Error
  case class ValidationError(invalidFields: List[String]) extends Error
  case class DbError(message: String) extends Error

  case class Unemployed(months: Int)
  case class Employed(title: String, companyName: String)
  case class Person(name: String, job: Either[Unemployed, Employed])

  case class Employer(name: String, offers401k: Boolean)



  //constructor
  class Foo(str: String)
  //instantiate a class
  val foo = new Foo("foo")

  //type constructor
  //Option is considered type * => * or Kind to Kind
  val invalidOption: Option = ???

  //the "I don't care what type" constructor.  Not great scala,
  val doNotCareOption: Option[_] = Some(Unit)
  doNotCareOption match {
    case None => ???
    case Some(x) => x
  }

  //to use option we must specify a type
  val validOption: Option[String] = ???

  //just an alias
  type OptionOfT[T] = Option[T]

  // takes 2 Types to make an either
  val invalidEither:  Either = ???
  val invalidEither2:  Either[Error] = ???

  //however, if we know one type, we can partially define the type
  type ErrorEither[T] = Either[Error, T]
  type RightErrorEither[T] = Either[T, Error] //not idiomatic scala


  def getPerson2(id: Int): Either[Error, Option[Person]] = ???
  def getEmployer2(name: String): Either[Error, Option[Employer]] = ???

  // we can redefine based on "partiall defined types"
  def getPerson3(id: Int): ErrorEither[Option[Person]] = ???
  def geEmployer3(name: String): ErrorEither[Option[Employer]] = ???

  //we can go one step further
  type ErrorEitherOptional[T] = Either[Error, Option[T]]

  def getPerson4(id: Int): ErrorEitherOptional[Person] = ???
  def getEmployer4(name: String): ErrorEitherOptional[Employer] = ???

  //bing it back to for comprehension
  // This is Ugly

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

  // Create a wrapper to take care of the ugly
  case class OptionT[A](value: Either[Error, Option[A]]) {

    // map is ALWAYS defined line this
    def map[B](f: A => B): OptionT[B] = {
      value match {
        case Left(err) => OptionT(Left(err))
        case Right(opt) => opt match {
          case None => OptionT(Right(None))
          case Some(a) => OptionT(Right(Some(f(a))))
        }
      }
    }

    // flatMap is ALWAYS defined line this
    def flatMap[B](f:  A => OptionT[B]): OptionT[B] = {
      value match {
        case Left(err) => OptionT(Left(err))
        case Right(opt) => opt match {
          case None => OptionT(Right(None))
          case Some(a) => f(a)
        }
      }
    }
  }

  //results in cleaner code
  val result2 = for {
    person <- OptionT(getPerson2(1))
    employer <- OptionT(getEmployer2(person.name))
  } yield (person, employer)
  result2.value


  //embrace the monad transformer
  def getPerson5(id: Int): OptionT[Person] = ???
  def getEmployer5(name: String): OptionT[Employer] = ???

  val result3 = for {
    person <- getPerson5(1)
    employer <- getEmployer5(person.name)
  } yield (person, employer)
  result3


  // F[_] is any class that is a type constructor with one parameter.
  // Eg, Option, Either[Error,*], Future
  case class EitherT[F[_], A, B](value: F[Either[A, B]]) {
    def map[C](f: B => C): EitherT[F[_],A,C] = ???
    def flatMap[C](f: B => EitherT[F[_],A,C]): EitherT[F[_],A,C] = ???
  }








  // which brings us to AsyncServiceResult, A specific definition of EitherT with Future as it's Wrapper
  type AsyncServiceResult[T] = EitherT[Future, Error, T]

  def getPersonAsync(id: Int): AsyncServiceResult[Person]
  def getEmployerAsync(name: String): AsyncServiceResult[Employer]

  val asyncResult = for {
    person <- getPersonAsync(1)
    employer <- getEmployerAsync(person.name)
  } yield (person, employer)
  asyncResult.value




}
