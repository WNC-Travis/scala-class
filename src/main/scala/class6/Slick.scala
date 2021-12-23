package class6

import com.github.tminglei.slickpg.ExPostgresProfile
import slick.basic.Capability
//import slick.jdbc.PostgresProfile.api._
import slick.sql.FixedSqlAction

import scala.concurrent.duration.DurationInt
import scala.concurrent.{ Await, ExecutionContext, Future }

trait MyPostgresProfile extends ExPostgresProfile {

  // Add back `capabilities.insertOrUpdate` to enable native `upsert` support; for postgres 9.5+
  override protected def computeCapabilities: Set[Capability] =
    super.computeCapabilities + slick.jdbc.JdbcCapabilities.insertOrUpdate

  override val api: MyAPI.type = MyAPI

  object MyAPI extends API
}

object MyPostgresProfile extends MyPostgresProfile

object Slick extends App {

  import MyPostgresProfile.api._

  implicit val executionContext = ExecutionContext.global

  case class ParticipantRow(participantId: Option[Int], fullName: String)

  class ParticipantTable(tag: Tag) extends Table[ParticipantRow](tag, "participant") {
    def participantId = column[Int]("participant_id", O.PrimaryKey, O.AutoInc)
    def fullName = column[String]("full_name")
    def * = (participantId.?, fullName) <> (ParticipantRow.tupled, ParticipantRow.unapply)
  }

  case class PositionsRow(positionsId: Option[Int], participantId: Int, accountName: String, funds: BigDecimal)

  class PositionsTable(tag: Tag) extends Table[PositionsRow](tag, "positions") {
    def positionsId = column[Int]("positions_id", O.PrimaryKey, O.AutoInc)
    def participantId = column[Int]("participant_id")
    def accountName = column[String]("account_name")
    def funds = column[BigDecimal]("funds")
    def * = (positionsId.?, participantId, accountName, funds) <> (PositionsRow.tupled, PositionsRow.unapply)
  }

  val db = Database.forConfig("mydb")

  val participantQuery = TableQuery[ParticipantTable]
  val positionsQuery = TableQuery[PositionsTable]

  def print(action: FixedSqlAction[_, _, _]): Unit = {
    println(action.statements)
  }

  def ex[R, S <: NoStream, E <: Effect](action: DBIOAction[R, S, E]) = {
    val result = Await.result(db.run(action), 10.seconds)
    println(result)
    result
  }

  /*** BEGIN ****/

//  val query = participantQuery.filter(_.fullName === "Bob Young").result // TOOD: Prepared statement?
//  ex(query)

//  ex(participantQuery.filter(t => t.fullName.like("Bob%")).filter(_.fullName.like("%Young")).result)

//  val compiledQuery = Compiled( (first: Rep[String], last: Rep[String]) => {
//    val percent: Rep[String] = "%"
//    participantQuery.filter(_.fullName.like(first ++ "%")).filter(_.fullName.like(percent ++ last))
//  })
//  ex(compiledQuery("Bob", "Young").result)

//  ex(participantQuery.filter(_.fullName.inSetBind(Seq("Bob", "Jose", "Travis"))).result)
//  ex(participantQuery.filter(_.fullName.inSet(Seq("Bob", "Jose"))).result)

//  ex(participantQuery.distinctOn(_.participantId).result)
//  ex(participantQuery.sortBy(_.fullName).take(15).drop(1).result)

  val newParticipant = ParticipantRow(Some(5), "Participant New Name")

  val insertQuery = participantQuery returning participantQuery.map(_.participantId) into (
      (
          participantRow,
          id
      ) => participantRow.copy(participantId = Some(id))
  )
//  ex( (participantQuery += newParticipant) )

  ex(insertQuery.insertOrUpdate(newParticipant))

//  val copiedNewParticipant = (insertQuery += newParticipant).flatMap(result => {
//    positionsQuery += PositionsRow(None, result.participantId.get, "main", BigDecimal(10))
//  }).transactionally
//  ex(copiedNewParticipant)

//  ex(participantQuery.result)

  //update on table and use the new foreign key from another

//  positionsQuery.join(participantQuery).on(_.participantId === _.participantId)

//  val result: (ParticipantRow, List[PositionsTable]) = ???

//  This might be the way of creating the prepared statements https://scala-slick.org/doc/3.1.1/api/index.html#slick.lifted.Compiled
//    Kurt Hardee to Everyone (10:40 AM)
//  When I have done the one-to-many relations in the past, I have used for-comprehensions to step through.

// will not run, group by does not currently work in PG driver.
//  val query1 = participantQuery
//    .join(positionsQuery)
//    .on(_.participantId === _.participantId)
//    .result
//
//  val query2 = participantQuery
//    .flatMap(participant => {
//      positionsQuery.filter(_.participantId === participant.participantId)
//    })
//    .result
//    .transactionally
//
//  val query3 = (for {
//    participant <- participantQuery
//    positions <- positionsQuery.filter(_.participantId === participant.participantId)
//    // account <- accountTable.filter(...)
//  } yield (participant, positions)).result.transactionally
//
//  ex(query3)

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
