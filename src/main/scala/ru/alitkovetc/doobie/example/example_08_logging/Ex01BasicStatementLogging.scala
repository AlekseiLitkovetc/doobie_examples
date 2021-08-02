package ru.alitkovetc.doobie.example.example_08_logging

import doobie._
import doobie.implicits._
import ru.alitkovetc.doobie.example.{WorldTransactor, WorldTransactorWithYOLO}

object Ex01BasicStatementLogging extends App with WorldTransactorWithYOLO {

  import yolo._

  def byName(pat: String) = {
    sql"select name, code from country where name like $pat"
      .queryWithLogHandler[(String, String)](LogHandler.jdkLogHandler)
      .to[List]
      .quick
  }

  byName("U%").unsafeRunSync()

}
