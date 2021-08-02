package ru.alitkovetc.doobie.example.example_10_quill_integration

import io.getquill.{idiom => _, _}
import doobie.quill.DoobieContext
import ru.alitkovetc.doobie.example.WorldTransactorWithYOLO

object Ex01QuillIntegration extends App with WorldTransactorWithYOLO {

  val dc = new DoobieContext.Postgres(Literal) // Literal naming scheme

  import dc._
  import yolo._

  case class Country(code: String, name: String, population: Int)

  // 01 example - query
  val quote1 = quote { query[Country].filter(_.code == "GBR") }
  run(quote1).quick.unsafeRunSync()

  // 02 example - simple update
  val update1 = quote { query[Country].filter(_.name like "U%").update(_.name -> "foo") }
  run(update1).quick//.unsafeRunSync()

  // 03 example - batch update
  val update2 = quote {
    liftQuery(List("U%", "A%")).foreach { pat =>
      query[Country].filter(_.name like pat).update(_.name -> "foo")
    }
  }
  run(update2).quick//.unsafeRunSync()

}
