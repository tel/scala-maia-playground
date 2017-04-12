package com.jspha.maia

import com.jspha.maia.Example.Person

import scala.language.higherKinds
import com.jspha.maia.language._
import shapeless._
import shapeless.labelled._
import shapeless.ops.hlist.Mapped

abstract class Request[T[_ <: Builder]] {
  type Spec <: HList
  type Fields <: HList
  val generic: LabelledGeneric.Aux[T[Request.Bld], Spec]
  val mapped: Mapped.Aux[Spec, Request.F, Fields]
  val record: Fields
}

object Request {

  trait Bld extends Builder {
    trait Mk[Tgt <: Target, Conf <: Config]
  }

  sealed trait F[_]
  case class F_X[N <: Symbol, Tgt <: Target, Conf <: Config]()
      extends F[FieldType[N, Bld#Mk[Tgt, Conf]]]

  def x[N <: Symbol, Tgt <: Target, Conf <: Config]()
    : F[FieldType[N, Bld#Mk[Tgt, Conf]]] = F_X()

  val nameWit = Witness('name)
  val incomeWit = Witness('income)

  val exReq: Request[Person] = apply[Person](
    x[nameWit.T, Atomic[String], ConfigNil]() ::
      x[incomeWit.T, Atomic[Int], ConfigNil]() ::
      HNil
  )

  class PartiallyApplied[T[_ <: Builder]] {
    def apply[Spec0 <: HList, Fields0 <: HList](record0: Fields0)(
        implicit generic0: LabelledGeneric.Aux[T[Request.Bld], Spec0],
        mapped0: Mapped.Aux[Spec0, Request.F, Fields0]
    ): Request[T] = new Request[T] {
      type Spec = Spec0
      type Fields = Fields0
      val generic = generic0
      val mapped = mapped0
      val record = record0
    }
  }

  def apply[T[_ <: Builder]]: PartiallyApplied[T] =
    new PartiallyApplied[T]

}
