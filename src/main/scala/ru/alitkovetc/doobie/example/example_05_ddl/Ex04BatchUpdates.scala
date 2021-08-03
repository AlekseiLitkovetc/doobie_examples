package ru.alitkovetc.doobie.example.example_05_ddl

import doobie._
import ru.alitkovetc.doobie.example.WorldTransactorWithYOLO

object Ex04BatchUpdates extends App with WorldTransactorWithYOLO {

  import yolo._

  case class Person(id: Long, name: String, age: Option[Short])

  type PersonInfo = (String, Option[Short])

  def insertMany(ps: List[PersonInfo]): ConnectionIO[Int] = {
    val sql = "insert into person (name, age) values (?, ?)"
    Update[PersonInfo](sql).updateMany(ps)
  }

  // Some rows to insert
  val data = List[PersonInfo](
    ("Frank", Some(12)),
    ("Daddy", None))

  insertMany(data).quick.unsafeRunSync()

  import fs2.Stream

  def insertMany2(ps: List[PersonInfo]): Stream[ConnectionIO, Person] = {
    val sql = "insert into person (name, age) values (?, ?)"
    Update[PersonInfo](sql).updateManyWithGeneratedKeys[Person]("id", "name", "age")(ps)
  }

  // Some rows to insert
  val data2 = List[PersonInfo](
    ("Banjo",   Some(39)),
    ("Skeeter", None),
    ("Jim-Bob", Some(12)))

  insertMany2(data2).quick.unsafeRunSync()

}
