package ru.alitkovetc.doobie.example.example_05_ddl

import doobie._
import doobie.implicits._
import ru.alitkovetc.doobie.example.WorldTransactorWithYOLO

object Ex03RetrievingResults extends App with WorldTransactorWithYOLO {

  import yolo._

  case class Person(id: Long, name: String, age: Option[Short])

  def insert2(name: String, age: Option[Short]): ConnectionIO[Person] =
    for {
      _ <- sql"insert into person (name, age) values ($name, $age)".update.run
      id <- sql"select lastval()".query[Long].unique
      p <- sql"select id, name, age from person where id = $id".query[Person].unique
    } yield p

  insert2("Jimmy", Some(42)).quick.unsafeRunSync()

  def insert2_H2(name: String, age: Option[Short]): ConnectionIO[Person] =
    for {
      id <- sql"insert into person (name, age) values ($name, $age)"
        .update
        .withUniqueGeneratedKeys[Int]("id")
      p <- sql"select id, name, age from person where id = $id"
        .query[Person]
        .unique
    } yield p

  insert2_H2("Ramone", Some(42)).quick.unsafeRunSync()

  def insert3(name: String, age: Option[Short]): ConnectionIO[Person] = {
    sql"insert into person (name, age) values ($name, $age)"
      .update
      .withUniqueGeneratedKeys("id", "name", "age")
  }

  insert3("Elvis", None).quick.unsafeRunSync()

  val up = {
    sql"update person set age = age + 1 where age is not null"
      .update
      .withGeneratedKeys[Person]("id", "name", "age")
  }

  up.quick.unsafeRunSync()
  up.quick.unsafeRunSync()
}
