package ru.alitkovetc.doobie.example.example_08_logging

import doobie._
import doobie.implicits._
import ru.alitkovetc.doobie.example.WorldTransactorWithYOLO

object Ex02ImplicitLogging extends App with WorldTransactorWithYOLO {

  import yolo._

  implicit val han = LogHandler.jdkLogHandler

  def byName2(pat: String) = {
    sql"select name, code from country where name like $pat"
      .query[(String, String)] // handler will be picked up here
      .to[List]
      .quick
  }

  byName2("U%").unsafeRunSync()

}
