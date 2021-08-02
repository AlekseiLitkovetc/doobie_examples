package ru.alitkovetc.doobie.example.example_09_custom_mappings

import doobie._

import java.awt.Point

object Ex03ColumnVectorMappings {

  implicit val pointRead: Read[Point] =
    Read[(Int, Int)].map { case (x, y) => new Point(x, y) }

  implicit val pointWrite: Write[Point] =
    Write[(Int, Int)].contramap(p => (p.x, p.y))

}
