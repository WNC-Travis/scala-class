package class6

import scala.concurrent.{Await, ExecutionContext, Future}
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.duration.Duration
import scala.io.Source

object CreateTables extends App {
  implicit val executionContext = ExecutionContext.global
  val db = Database.forConfig("mydb")
  val inserts_sql = Source.fromResource("tables.sql").mkString

  val run = db.run(sqlu"$inserts_sql")
  Await.ready(run, Duration.Inf)


}
