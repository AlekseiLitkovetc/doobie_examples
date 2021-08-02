package ru.alitkovetc.doobie.example.example_05_ddl

import cats.implicits.catsSyntaxTuple2Semigroupal
import doobie._
import doobie.implicits._
import ru.alitkovetc.doobie.example.WorldTransactor

object Ex01DataDefinition extends App with WorldTransactor {

  val drop: ConnectionIO[Int] =
    sql"""
    DROP TABLE IF EXISTS person
  """.update.run

  val create: ConnectionIO[Int] =
    sql"""
    CREATE TABLE person (
      id   SERIAL,
      name VARCHAR NOT NULL UNIQUE,
      age  SMALLINT
    )
  """.update.run

  (drop, create).mapN(_ + _).transact(xa).unsafeRunSync()

}
