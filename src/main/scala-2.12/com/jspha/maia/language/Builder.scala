package com.jspha.maia.language

import scala.language.higherKinds

trait Builder {
  type Mk[Tgt <: Target, C <: Config]
  type Atomic[A] = Mk[com.jspha.maia.language.Atomic[A], ConfigNil]
  type Nested[A] = Mk[com.jspha.maia.language.Atomic[A], ConfigNil]
}
