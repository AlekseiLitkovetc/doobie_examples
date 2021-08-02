package ru.alitkovetc.doobie.example.example_02_selecting_data

import doobie.implicits.toSqlInterpolator
import ru.alitkovetc.doobie.example.WorldTransactorWithYOLO
import shapeless._
import shapeless.HNil
import shapeless.record.Record

object Ex03MultiColumnQueries extends App with WorldTransactorWithYOLO {

  import yolo._

  sql"select code, name, population, gnp from country"
    .query[(String, String, Int, Option[Double])]
    .stream
    .take(5)
    .quick
    .unsafeRunSync()

  sql"select code, name, population, gnp from country"
    .query[String :: String :: Int :: Option[Double] :: HNil]
    .stream
    .take(5)
    .quick
    .unsafeRunSync()

  type Rec = Record.`'code -> String, 'name -> String, 'pop -> Int, 'gnp -> Option[Double]`.T

  sql"select code, name, population, gnp from country"
    .query[Rec]
    .stream
    .take(5)
    .quick
    .unsafeRunSync()

  case class Country(code: String, name: String, pop: Int, gnp: Option[Double])

  sql"select code, name, population, gnp from country"
    .query[Country]
    .stream
    .take(5)
    .quick
    .unsafeRunSync()

  case class Code(code: String)
  case class Country2(name: String, pop: Int, gnp: Option[Double])

  sql"select code, name, population, gnp from country"
    .query[(Code, Country2)]
    .stream
    .take(5)
    .quick
    .unsafeRunSync()
}
