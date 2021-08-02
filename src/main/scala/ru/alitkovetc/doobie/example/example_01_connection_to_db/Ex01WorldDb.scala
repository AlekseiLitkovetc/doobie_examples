package ru.alitkovetc.doobie.example.example_01_connection_to_db

import cats.effect.{ContextShift, IO}
import doobie._
import doobie.implicits._
import cats.implicits._
import doobie.util.ExecutionContexts
import org.specs2.control.Properties.aProperty

object Ex01WorldDb extends App {

  private implicit val cs: ContextShift[IO] = IO.contextShift(ExecutionContexts.synchronous)

  private val xa = Transactor.fromDriverManager[IO](
    "org.postgresql.Driver",
    "jdbc:postgresql://localhost:5433/world",
    "postgres", "password"
  )

  private val program1: ConnectionIO[Int] = 42.pure[ConnectionIO]
  private val program2: ConnectionIO[Int] = sql"select 42".query[Int].unique
  private val program3: ConnectionIO[List[Int]] = for {
    result1 <- program1
    result2 <- program2
  } yield List(result1, result2)

  program1.transact(xa).unsafeRunSync().foreach(println)
  program2.transact(xa).unsafeRunSync().foreach(println)
  program3.transact(xa).unsafeRunSync().foreach(println)

}
