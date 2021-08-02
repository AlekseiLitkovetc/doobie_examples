package ru.alitkovetc.doobie.example.example_09_custom_mappings

import doobie.Get
import doobie.util.Put

import scala.annotation.tailrec

object NatModule {

  sealed trait Nat
  case object Zero extends Nat
  case class  Succ(n: Nat) extends Nat

  def toInt(n: Nat): Int = {
    @tailrec
    def go(n: Nat, acc: Int): Int =
      n match {
        case Zero    => acc
        case Succ(n) => go(n, acc + 1)
      }
    go(n, 0)
  }

  def fromInt(n: Int): Nat = {
    @tailrec
    def go(n: Int, acc: Nat): Nat =
      if (n <= 0) acc else go(n - 1, Succ(acc))
    go(n, Zero)
  }

}

object Ex02SingleColumnTypeMappings {

  import NatModule._

  implicit val natGet: Get[Nat] = Get[Int].map(fromInt)
  implicit val natPut: Put[Nat] = Put[Int].contramap(toInt)

  implicit val natGet2: Get[Nat] = Get[Int].tmap(fromInt)
  implicit val natPut2: Put[Nat] = Put[Int].tcontramap(toInt)

}
