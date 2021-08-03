package ru.alitkovetc.doobie.example.example_02_selecting_data

import doobie._
import doobie.implicits.{toSqlInterpolator, _}
import ru.alitkovetc.doobie.example.WorldTransactor

object Ex01Limit extends App with WorldTransactor {

  // Reading Rows into Collections
  private val program1: ConnectionIO[List[String]] = sql"select name from country"
    .query[String]
    .to[List]

  program1.transact(xa)
    .unsafeRunSync()
    .take(5)
    .foreach(println)

  // Internal Streaming
  private val program2: ConnectionIO[List[String]] = sql"select name from country"
    .query[String]
    .stream
    .take(5)
    .compile
    .toList

  program2.transact(xa)
    .unsafeRunSync()
    .foreach(println)

  private val program3 = sql"select name from country limit 5"
    .query[String]
    .to[List]

  program3.transact(xa)
    .unsafeRunSync()
    .foreach(println)

}
