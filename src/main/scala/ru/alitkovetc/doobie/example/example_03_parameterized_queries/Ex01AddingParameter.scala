package ru.alitkovetc.doobie.example.example_03_parameterized_queries

import cats.data.NonEmptyList
import doobie._
import doobie.implicits.toSqlInterpolator
import ru.alitkovetc.doobie.example.WorldTransactorWithYOLO

object Ex01AddingParameter extends App with WorldTransactorWithYOLO {

  import yolo._

  case class Country(code: String, name: String, pop: Int, gnp: Option[Double])

  // Adding a Parameter

  def biggerThan(minPop: Int) = sql"""
    select code, name, population, gnp
    from country
    where population > $minPop
  """.query[Country]

  biggerThan(150000000).quick.unsafeRunSync()

  // Multiple Parameters

  def populationIn(range: Range) = sql"""
    select code, name, population, gnp
    from country
    where population > ${range.min}
    and   population < ${range.max}
  """.query[Country]

  populationIn(150000000 to 200000000).quick.unsafeRunSync()

  // Dealing with IN Clauses

  def populationIn(range: Range, codes: NonEmptyList[String]) = {
    val q =
      fr"""
      select code, name, population, gnp
      from country
      where population > ${range.min}
      and   population < ${range.max}
      and   """ ++ Fragments.in(fr"code", codes) // code IN (...)
    q.query[Country]
  }

  populationIn(100000000 to 300000000, NonEmptyList.of("USA", "BRA", "PAK", "GBR")).quick
    .unsafeRunSync()

}
