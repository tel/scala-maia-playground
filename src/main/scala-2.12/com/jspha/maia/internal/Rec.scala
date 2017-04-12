package com.jspha.maia.internal

import scala.language.higherKinds
import shapeless._

sealed trait Rec[+F[_ <: FieldSpec], Z <: HList]

object Rec {

  final case class Cons[+F[_ <: FieldSpec], H <: FieldSpec, T <: HList](
      h: F[H],
      t: Rec[F, T])
      extends Rec[F, H :: T] {
    def :^:[G[X <: FieldSpec] >: F[X], A <: FieldSpec](h: G[A]) =
      Cons(h, this)
  }

  val :^: = Cons

  sealed class Nil extends Rec[Nothing, HNil] {
    def :^:[F[_], A <: FieldSpec](h: F[A]) =
      Cons(h, this)
  }

  object Nil extends Nil

  sealed trait FromLabelledGeneric[F[_ <: FieldSpec], T] {
    type Spec <: Z
    def to(t: T): Rec[F, Z]
    def from(rec: Rec[F, Z]): T
  }

  object FromLabelledGeneric {
    type Aux[F[_ <: FieldSpec], T, Z <: HList, Out0 <: Rec[F, Z]] =
      FromLabelledGeneric[F, T, Z] { type Out = Out0 }

    implicit def _FromLabelledGeneric(implicit generic: LabelledGeneric
    .Aux[T, Gen]): Aux[F, T, Spec, ]

  }

}
