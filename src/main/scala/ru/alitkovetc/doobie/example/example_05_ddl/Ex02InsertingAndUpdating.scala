package ru.alitkovetc.doobie.example.example_05_ddl

import doobie._
import doobie.implicits._
import ru.alitkovetc.doobie.example.WorldTransactorWithYOLO

object Ex02InsertingAndUpdating extends App with WorldTransactorWithYOLO {

  import yolo._

  // Inserting
  def insert1(name: String, age: Option[Short]): Update0 =
    sql"insert into person (name, age) values ($name, $age)".update

  insert1("Alice", Some(12)).run.transact(xa).unsafeRunSync()
  insert1("Bob", None).quick.unsafeRunSync() // switch to YOLO mode

  // Querying
  case class Person(id: Long, name: String, age: Option[Short])
  sql"select id, name, age from person".query[Person].quick.unsafeRunSync()

  // Updating
  sql"update person set age = 15 where name = 'Alice'".update.quick.unsafeRunSync()

  // Querying
  sql"select id, name, age from person".query[Person].quick.unsafeRunSync()


  def insert2(name: String, age: Option[Short]): ConnectionIO[Person] =
    for {
      _  <- sql"insert into person (name, age) values ($name, $age)".update.run
      id <- sql"select lastval()".query[Long].unique
      p  <- sql"select id, name, age from person where id = $id".query[Person].unique
    } yield p
  insert2("Jimmy", Some(42)).quick.unsafeRunSync()
}
