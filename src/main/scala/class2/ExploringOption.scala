package class2

object ExploringOption {

  /*
  trait Option[T]  //Type Constructor
  object None extends Option[Nothing]
  case class Some[T](t: T) extends Option[T]
   */

  //.get is an anti-pattern
  val x1 = Some(1).get // Type Int
  val x2 = None.get    // Type Nothing

  val x3 = if (true) Some(1) else None // Option[Int]


  case class Job(title: String, salary: Option[Int])
  case class Person(name: String, job: Option[Job])
  def person1: Option[Person] = Some(Person("John", Some(Job("Engineer", Some(50000)))))
  def person2: Option[Person] = None
  def person3: Option[Person] = Some(Person("John", None))

  person1.map(person => person.name)
  person2.map(person => person.name)

  //nested option
  val r1 = person1.map(_.job)
  val r2 = person1.map(_.job.map(_.salary))

  //flatten
  val monthlySalaryDeSugar = person3
    .flatMap(person => person.job)
    .flatMap(job => job.salary)
    .map(salary => salary / 12)

  val monthlySalaryDeSugar = person3.flatMap(person => {
    val introduction = "Hello, my name is" + person.name
    person.job.flatMap(job => job.salary).map(salary => (introduction, salary / 12) )
  })


  // for comprehension
  val monthlySalary = for {
    person <- person3
    introduction = "Hello, my name is" + person.name
    job <- person.job
    salary <- job.salary
  } yield {
    salary / 12
  }

  // All flatMap's must "succeed"
  // All right side values must be of the same "higher kinded type".


  // Some notable Option methods.

  val o1 = monthlySalary.getOrElse(0)
  val o2 = monthlySalary.exists(_ > 1000) // must be some and true
  val o3 = monthlySalary.forall(_ > 1000) // either none or true
  val o4 = monthlySalary.fold[Int](0)(x => x)
  val o5 = monthlySalary match {
    case Some(sal) => sal
    case None => 0
  }
  val o6 = monthlySalary.orElse(Some(2))
  val o7 = monthlySalary.foreach(println)
  val o8 = monthlySalary.mkString

  // Deconstructor
  val o9 = person1.map {
    case Person(name, None) => ???
    case Person(name, Some(job)) => s"$name : ${job}"
  }


  // Anti Patterns
  val hasJob: Boolean = true
  val p1 =
    if (hasJob || monthlySalary.isDefined) monthlySalary.get
    else 0



}
