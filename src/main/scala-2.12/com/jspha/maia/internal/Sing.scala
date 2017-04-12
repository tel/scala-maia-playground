package com.jspha.maia.internal

import shapeless._

trait Sing[_]

object Sing {

  def apply[A](implicit sing: Sing[A]): Sing[A] = sing

  case object S_HNil extends Sing[HNil]

  case class S_HCons[H, T <: HList](head: Sing[H], tail: Sing[T])
      extends Sing[H :: T]

  implicit val HNilSing: Sing[HNil] =
    S_HNil

  implicit def HConsSing[H, T <: HList](implicit head: Sing[H],
                                        tail: Sing[T]): Sing[H :: T] =
    S_HCons(head, tail)

}
