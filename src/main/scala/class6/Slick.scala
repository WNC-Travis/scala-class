package class6

import slick.jdbc.PostgresProfile.api._
import slick.lifted.TableQuery
import slick.memory.HeapBackend.Column
import slick.sql.FixedSqlAction

import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, ExecutionContext, Future}


object Slick extends App {

  implicit val executionContext = ExecutionContext.global

  case class ParticipantRow(participantId: Option[Int], fullName: String)

  class ParticipantTable(tag: Tag) extends Table[ParticipantRow](tag, "participant2") {
    def participantId = column[Int]("participant_id", O.PrimaryKey, O.AutoInc)
    def fullName = column[String]("first_name")
    def * = (participantId.?, fullName) <> (ParticipantRow.tupled, ParticipantRow.unapply)
  }

  case class PositionsRow(positionsId: Option[Int], participantId: Int, accountName: String, funds: BigDecimal)

  class PositionsTable(tag: Tag) extends Table[PositionsRow](tag, "positions2") {
    def positionsId = column[Int]("positions_id", O.PrimaryKey, O.AutoInc)
    def participantId = column[Int]("participant_id")
    def accountName = column[String]("account_name")
    def funds = column[BigDecimal]("funds")
    def * = (positionsId.?, participantId, accountName, funds) <> (PositionsRow.tupled, PositionsRow.unapply)
  }

  val db = Database.forConfig("mydb")

  val participantQuery = TableQuery[ParticipantTable]
  val positionsQuery = TableQuery[PositionsTable]

  def print(action: FixedSqlAction[_,_,_]): Unit = {
    println(action.statements)
  }

  val query = participantQuery.filter(_.fullName === "Bob Young").result // TOOD: Prepared statement?
  println(Await.result(db.run(query), 10.seconds))

  participantQuery.filter(_.fullName.like("Bob" + "%")).filter(_.fullName.like("%Young"))

  val compiledQuery = Compiled( (first: Rep[String], last: Rep[String]) => {
    first
    participantQuery.filter(_.fullName.like(first ++ "%")).filter(_.fullName.like(Rep("%") ++ last))
  })
  println(Await.result(db.run(compiledQuery("Bob%", "%Young").result), 5.seconds))

//  print(participantQuery.filter(_.fullName.inSetBind(Seq("Bob", "Jose", "Travis"))).result)
//  print(participantQuery.filter(_.fullName.inSet(Seq("Bob", "Jose"))).result)
//  print(participantQuery.distinctOn(_.participantId).result)
//  print(participantQuery.take(15).sortBy(_.fullName.nullsLast).result)

//  print( (participantQuery += ParticipantRow(None, "Bob")) )

//  positionsQuery.join(participantQuery).on(_.participantId === _.participantId)

//  val result: (ParticipantRow, List[PositionsTable]) = ???

//  This might be the way of creating the prepared statements https://scala-slick.org/doc/3.1.1/api/index.html#slick.lifted.Compiled
//    Kurt Hardee to Everyone (10:40 AM)
//  When I have done the one-to-many relations in the past, I have used for-comprehensions to step through.

// will not run, group by does not currently work in PG driver.
//    participantQuery.join(positionsQuery).on(_.participantId === _.participantId)
//      .groupBy(_._1)
//      .result



//    val result = db.run(participantQuery.join(positionsQuery).on(_.participantId === _.participantId).result)
//      .map(x => x.groupBy{ case (participant, _) => participant})
//      .map(result => result.view.mapValues(v => v.map(_._2)).toMap)
//
//    val futureValue =Await.result(result, 5.seconds)
//    println(futureValue)

  class Participant2(firstName: String, positions: Seq[PositionsRow])


//    val partPos: Seq[(ParticipantRow, Seq[PositionsRow])] = result.value

  //TODO Look into unit testing queries.


  // TODO More on many to one relationship.

//  import scala.concurrent.ExecutionContext.Implicits.global
//  val flatMap = for {
//    p <- participantQuery.map(_.participantId).result
//    l <- participantQuery.length.result
//  } yield (p,l)



//  print(participantQuery.flatMap(t => positionsQuery.filter(_.participantId === t.participantId)).result)
//
//
//  // Seq, sets
//
//  val u1 = (1,BigDecimal(30))
//  val u2 = (2,BigDecimal(40))
//
//  val fundUpdate1 = positionsQuery.filter(_.positionsId === u1._1).map(_.funds)
//    .update(u1._2)
//
//  val fundUpdate2 = positionsQuery.filter(_.positionsId === u2._1).map(_.funds)
//    .update(u2._2)
//
//  print(fundUpdate1)
//  print(fundUpdate2)
//
//  val tr = DBIO.seq(
//    fundUpdate1,
//    fundUpdate2
//  ).transactionally
//
//  db.run(tr)





}
