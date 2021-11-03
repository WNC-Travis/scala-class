package class5

import scala.concurrent.{ExecutionContext, Future}


object BaseImplicit {
  //Implicit Class
  implicit class StringUtil(str: String) {
    def numberOfNumbers: Int = str.map(_.isDigit).length
  }

  def numberOfNonNumbers(implicit str: String) = {
    str.map(_.isDigit).length
  }

}


object ImplicitsAndThings {

  import BaseImplicit._
  implicit val ex: ExecutionContext = ???

  implicit val string = "1a2b3c4d"

  string.numberOfNumbers

  numberOfNonNumbers(string)

  //Look at future.
  val future = Future.successful(1)

  val futureToString: Int => Future[String] = i =>  Future.successful(i.toString)

  future.flatMap(futureToString)

  for {
    i <- future
    str <- futureToString(i)
  } yield str


}
