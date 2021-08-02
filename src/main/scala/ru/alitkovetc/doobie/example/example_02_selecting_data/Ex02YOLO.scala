package ru.alitkovetc.doobie.example.example_02_selecting_data

import doobie.implicits.toSqlInterpolator
import ru.alitkovetc.doobie.example.WorldTransactor

object Ex02YOLO extends App with WorldTransactor {

  private val yolo = xa.yolo
  import yolo._

  sql"select name from country"
    .query[String]
    .stream
    .take(5)
    .quick
    .unsafeRunSync()

}
