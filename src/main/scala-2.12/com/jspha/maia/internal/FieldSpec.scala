package com.jspha.maia.internal

import scala.language.higherKinds
import com.jspha.maia.language._
import shapeless.labelled._

sealed trait FieldSpec

object FieldSpec {

  sealed trait Of[Cs <: Config.Set, Tgt <: Target] extends FieldSpec

  sealed trait FromFieldType[Ft] {
    type Out <: FieldSpec
  }

  object FromFieldType {
    type Aux[Ft, Out0] = FromFieldType[Ft] { type Out = Out0 }

    implicit def _FromFieldType[K <: Symbol,
                                C <: Config,
                                Tgt <: Target,
                                Set <: Config.Set](
        implicit cstepsReducer: Config.FromConfig.Aux[K, C, Set]
    ): Aux[FieldType[K, Tupled.Mk[Tgt, C]], Of[Set, Tgt]] =
      new FromFieldType[FieldType[K, Tupled.Mk[Tgt, C]]] {
        type Out = Of[Set, Tgt]
      }
  }

}
