package com.jspha.maia.test.internal

import com.jspha.maia.language._
import com.jspha.maia.language.Cardinality._
import com.jspha.maia.internal.FieldSpec._
import com.jspha.maia.internal._
import shapeless._
import shapeless.labelled._

object FieldSpec {

  object FromFieldTypeTests {

    val defaultSymWitness = Witness('default)
    type Name0 = defaultSymWitness.T

    val someNameWitness = Witness('someName)
    type SomeName = someNameWitness.T

    val fftTest =
      implicitly[FromFieldType.Aux[
        FieldType[Name0,
                  Tupled.Mk[Atomic[String], Named[SomeName] & TakesArg[Int]]],
        Of[Config.Set.Of[SomeName, Config.HasArg[Int], Config.NoErr, One],
           Atomic[String]]]]

  }

}
