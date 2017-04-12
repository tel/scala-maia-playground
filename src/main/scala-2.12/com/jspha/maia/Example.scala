package com.jspha.maia

import com.jspha.maia.language._

import scala.language.higherKinds
import shapeless._

object Example {

  val incomeWitness = Witness('incomeOfPerson)
  sealed trait Currency

  case class Person[B <: Builder](
      name: B#Atomic[String],
      income: B#Mk[
        Atomic[Int],
        ConfigNil
      ]
  )

  case class City[S <: Builder](
      name: S#Atomic[Atomic[Int]],
      mayor: S#Atomic[Nested[Person]]
  )

}
