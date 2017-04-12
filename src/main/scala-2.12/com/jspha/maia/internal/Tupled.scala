package com.jspha.maia.internal

import com.jspha.maia.language._

sealed trait Tupled extends Builder {
  sealed trait Mk[Tgt <: Target, C <: Config]
}

object Tupled extends Tupled
