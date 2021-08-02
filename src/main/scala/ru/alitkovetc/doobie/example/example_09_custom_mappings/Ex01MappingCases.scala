package ru.alitkovetc.doobie.example.example_09_custom_mappings

import doobie._
import doobie.implicits._

object Ex01MappingCases extends App {

  // When do I need a custom type mapping?

  // 01 example
//  def nope(msg: String, ex: Exception): ConnectionIO[Int] =
//    sql"INSERT INTO log(message, detail) VALUES ($msg, $ex)".update.run

  // 02 example
//  case class LogEntry(msg: String, ex: Exception)
//
//  sql"SELECT message, detail FROM log".query[LogEntry]

}
