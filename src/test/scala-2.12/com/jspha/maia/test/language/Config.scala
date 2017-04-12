package com.jspha.maia.test.language

import shapeless._
import com.jspha.maia.language._
import com.jspha.maia.language.Cardinality._
import com.jspha.maia.language.Config._

object Config {

  val defaultSymWitness = Witness('default)
  type Name0 = defaultSymWitness.T

  val someNameWitness = Witness('someName)
  type SomeName = someNameWitness.T

  object FromConfigTests {

    val configNil =
      implicitly[
        FromConfig.Aux[Name0, ConfigNil, FromConfig.DefaultSetNamed[Name0]]]

    val configNamed =
      implicitly[FromConfig.Aux[Name0,
                                Named[SomeName],
                                Set.Of[SomeName, NoArg, NoErr, One]]]

    val configArg =
      implicitly[FromConfig.Aux[Name0,
                                TakesArg[Int],
                                Set.Of[Name0, HasArg[Int], NoErr, One]]]

    val configErr =
      implicitly[FromConfig.Aux[Name0,
                                GivesErr[String],
                                Set.Of[Name0, NoArg, HasErr[String], One]]]

    val configAndSimple =
      implicitly[
        FromConfig.Aux[Name0,
                       ConfigNil & ConfigNil,
                       FromConfig.DefaultSetNamed[Name0]]
      ]

    val configAndComplex =
      implicitly[
        FromConfig.Aux[Name0,
                       Named[SomeName] &
                         TakesArg[Int] &
                         GivesErr[String] &
                         HasCardinality[Many],
                       Set.Of[SomeName, HasArg[Int], HasErr[String], Many]]
      ]

    val configAndDoubleSpecification =
      implicitly[
        FromConfig.Aux[Name0,
                       // NOTE: The ands are "left biased"...
                       // just in case you do have repeats
                       Named[SomeName] &
                         TakesArg[Int] &
                         Named[Name0] &
                         GivesErr[String] &
                         TakesArg[String] &
                         HasCardinality[Many] &
                         GivesErr[Int] &
                         HasCardinality[Opt],
                       Set.Of[SomeName, HasArg[Int], HasErr[String], Many]]
      ]
  }

}
