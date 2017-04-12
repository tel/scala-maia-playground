package com.jspha.maia.language

import scala.language.higherKinds

sealed trait Cardinality {
  type Coll[A]
}

object Cardinality {

  sealed trait One extends Cardinality {
    type Coll[A] = A
  }

  sealed trait Opt extends Cardinality {
    type Coll[A] = Option[A]
  }

  sealed trait Many extends Cardinality {
    type Coll[A] = Vector[A]
  }

}
