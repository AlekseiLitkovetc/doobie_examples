package ru.alitkovetc.doobie.example.example_06_statement_fragments

import cats.implicits._
import doobie._
import doobie.implicits._
import ru.alitkovetc.doobie.example.WorldTransactorWithYOLO

object Ex02FragmentsModule extends App with WorldTransactorWithYOLO {

  import yolo._

  // Import some convenience combinators.
  import Fragments.{in, whereAndOpt}

  // Country Info
  case class Info(name: String, code: String, population: Int)

  // Construct a Query0 with some optional filter conditions and a configurable LIMIT.
  def select(name: Option[String], pop: Option[Int], codes: List[String], limit: Long) = {

    // Three Option[Fragment] filter conditions.
    val f1 = name.map(s => fr"name LIKE $s")
    val f2 = pop.map(n => fr"population > $n")
    val f3 = codes.toNel.map(cs => in(fr"code", cs))

    // Our final query
    val q: Fragment =
      fr"SELECT name, code, population FROM country" ++
        whereAndOpt(f1, f2, f3) ++
        fr"LIMIT $limit"

    // Construct a Query0
    q.query[Info]

  }

  select(None, None, Nil, 10).check.unsafeRunSync() // no filters
  select(Some("U%"), None, Nil, 10).check.unsafeRunSync() // one filter
  select(Some("U%"), Some(12345), List("FRA", "GBR"), 10).check.unsafeRunSync() // three filters

}
