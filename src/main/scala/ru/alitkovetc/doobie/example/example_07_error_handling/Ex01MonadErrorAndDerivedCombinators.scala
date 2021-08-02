package ru.alitkovetc.doobie.example.example_07_error_handling

import cats.implicits._
import doobie._
import doobie.implicits._
import ru.alitkovetc.doobie.example.WorldTransactorWithYOLO

object Ex01MonadErrorAndDerivedCombinators extends App with WorldTransactorWithYOLO {

  import yolo._

  val p: doobie.ConnectionIO[Int] = 42.pure[ConnectionIO]
  private val attempt: ConnectionIO[Either[Throwable, Int]] = p.attempt

  // Unique Constraint Violation
  List(
    sql"""DROP TABLE IF EXISTS person""",
    sql"""CREATE TABLE person (
          id    SERIAL,
          name  VARCHAR NOT NULL UNIQUE
        )"""
  ).traverse(_.update.quick).void.unsafeRunSync()

  case class Person(id: Int, name: String)

  def insert(s: String): ConnectionIO[Person] = {
    sql"insert into person (name) values ($s)"
      .update
      .withUniqueGeneratedKeys("id", "name")
  }

  insert("bob").quick.unsafeRunSync()

  try {
    insert("bob").quick.unsafeRunSync()
  } catch {
    case e: java.sql.SQLException =>
      println(e.getMessage)
      println(e.getSQLState)
  }

  import doobie.postgres._

  def safeInsert(s: String): ConnectionIO[Either[String, Person]] =
    insert(s).attemptSomeSqlState {
      case sqlstate.class23.UNIQUE_VIOLATION => "Oops!"
    }

  safeInsert("bob").quick.unsafeRunSync()

  safeInsert("steve").quick.unsafeRunSync()

}
