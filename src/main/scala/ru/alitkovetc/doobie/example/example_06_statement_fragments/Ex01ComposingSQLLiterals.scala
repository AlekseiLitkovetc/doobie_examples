package ru.alitkovetc.doobie.example.example_06_statement_fragments

import cats.implicits.toFoldableOps
import doobie._
import doobie.implicits._
import ru.alitkovetc.doobie.example.WorldTransactorWithYOLO

object Ex01ComposingSQLLiterals extends App with WorldTransactorWithYOLO {

  import yolo._

  // Sub-example 1
  val a = fr"select name from country"
  val b = fr"where code = 'USA'"
  val c = a ++ b // concatenation by ++

  c.query[String].unique.quick.unsafeRunSync()

  // Sub-example 2
  def whereCode(s: String) = fr"where code = $s"

  val fra = whereCode("FRA")

  (fr"select name from country" ++ fra).query[String].quick.unsafeRunSync()

  // Sub-example 3
  // Fragment.const performs no escaping of passed strings
  // Passing user-supplied data is an injection risk.
  def count(table: String) = (fr"select count(*) from" ++ Fragment.const(table)).query[Int].unique

  count("city").quick.unsafeRunSync()

  // Sub-example 4
  val countryCode: String = "NZL"
  val whereFragment: Fragment = fr"WHERE code = $countryCode"

  val frag = fr"SELECT name FROM country $whereFragment"

  frag.query[String].option.quick.unsafeRunSync()

  // Whitespace handling
  fr"IN (" ++ List(1, 2, 3).map(n => fr"$n").intercalate(fr",") ++ fr")"
  // Fragment("IN ( ? , ? , ? ) ")

  // note about `sql` interpolator
  fr0"IN (" ++ List(1, 2, 3).map(n => fr0"$n").intercalate(fr",") ++ fr")"
  // Fragment("IN (?, ?, ?) ")

}
