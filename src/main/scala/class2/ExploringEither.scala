package class2

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
  e2.right.get //Fails with Left

  val e4 = e3.map(i => i + 1)
  val e5 = e3.left.map(str => str + " World")

  case class Unemployed(months: Int)
  case class Hourly(hourlyPay: Int)
  case class Salary(salary: Int)
  case class Employed(title: String, salary: Either[Hourly, Salary])
  case class Person(name: String, job: Either[Unemployed, Employed])

  val p1 = Person("John", Right(Employed("Engineer", Right(Salary(50000)))))
  val p2 = Person("Bob", Right(Employed("Line CooK", Left(Hourly(15)))))






}
