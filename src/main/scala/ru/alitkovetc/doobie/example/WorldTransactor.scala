package ru.alitkovetc.doobie.example

import cats.effect.{ContextShift, IO, IOApp}
import doobie.Transactor
import doobie.util.ExecutionContexts
import doobie.util.transactor.Transactor.Aux

trait WorldTransactor {

  protected implicit val cs: ContextShift[IO] = IO.contextShift(ExecutionContexts.synchronous)

  // Our world database
  protected val xa: Aux[IO, Unit] = Transactor.fromDriverManager[IO](
    "org.postgresql.Driver",
    "jdbc:postgresql://localhost:5433/world",
    "postgres", "password"
  )

}
