package com.jspha.maia.language

import scala.language.higherKinds

sealed trait Target
sealed trait Atomic[A] extends Target
sealed trait Nested[T[_ <: Builder]] extends Target
