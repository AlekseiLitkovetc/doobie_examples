package ru.alitkovetc.doobie.example.example_04_typechecking_queries

import doobie.implicits.toSqlInterpolator
import ru.alitkovetc.doobie.example.WorldTransactorWithYOLO

object Ex01CheckingAQuery extends App with WorldTransactorWithYOLO {

  import yolo._

  case class Country(code: String, name: String, pop: Int, gnp: Option[Double])

  // Checking a Query

  def biggerThan(minPop: Short) = sql"""
    select code, name, population, gnp, indepyear
    from country
    where population > $minPop
  """.query[Country]

  biggerThan(0).check.unsafeRunSync()

  // Fix

  case class Country2(code: String, name: String, pop: Int, gnp: Option[BigDecimal])

  def biggerThan2(minPop: Int) = sql"""
    select code, name, population, gnp
    from country
    where population > $minPop
  """.query[Country2]

  biggerThan2(0).check.unsafeRunSync()

}
