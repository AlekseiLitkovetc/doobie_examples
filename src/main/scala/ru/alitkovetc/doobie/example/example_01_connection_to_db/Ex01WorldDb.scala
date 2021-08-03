package ru.alitkovetc.doobie.example.example_01_connection_to_db

import cats.effect.{ContextShift, IO}
import cats.implicits._
import doobie._
import doobie.implicits._
import doobie.util.ExecutionContexts
import org.specs2.control.Properties.aProperty

// Low-level API (doobie.free) - no resource safety, NULL checking, or type mapping
// High-level API (doobie.hi) - provides a safe subset of the JDBC API

object Ex01WorldDb extends App {

  // The most common type is ConnectionIO[A].
  // It specifies computations that take place in a context where a java.sql.Connection is available.
  private val program1: ConnectionIO[Int] = 42.pure[ConnectionIO]
  private val program2: ConnectionIO[Int] = sql"select 42".query[Int].unique

  // Transactor is a data type that knows how to connect to a database, hand out connections, and clean them up.
  // It can transform ConnectionIO ~> IO.
  // Specifically it gives us an IO that, when run, will connect to the database and execute single transaction.
  private implicit val cs: ContextShift[IO] = IO.contextShift(ExecutionContexts.synchronous)

  private val xa = Transactor.fromDriverManager[IO](
    "org.postgresql.Driver",
    "jdbc:postgresql://localhost:5433/world",
    "postgres", "password"
  )

  program1.transact(xa).unsafeRunSync().foreach(println)
  program2.transact(xa).unsafeRunSync().foreach(println)

  // program1 & program2 will be executed in one transaction
  private val program3: ConnectionIO[List[Int]] = for {
    result1 <- program1
    result2 <- program2
  } yield List(result1, result2)
  private val program4: ConnectionIO[List[Int]] = List(program1, program2).traverse(identity)

  program3.transact(xa).unsafeRunSync().foreach(println)
  program4.transact(xa).unsafeRunSync().foreach(println)

}
