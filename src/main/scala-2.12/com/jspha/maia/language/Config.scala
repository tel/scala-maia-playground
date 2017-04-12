package com.jspha.maia.language

sealed trait Config

sealed trait Named[S <: Symbol] extends Config
sealed trait TakesArg[A] extends Config
sealed trait GivesErr[E] extends Config
sealed trait HasCardinality[C <: Cardinality] extends Config
sealed trait ConfigNil extends Config
sealed trait &[Ca <: Config, Cb <: Config] extends Config

object Config {

  sealed trait ArgConf
  sealed trait HasArg[A] extends ArgConf
  sealed trait NoArg extends ArgConf

  sealed trait ErrConf
  sealed trait HasErr[E] extends ErrConf
  sealed trait NoErr extends ErrConf

  sealed trait Set

  object Set {
    sealed trait Of[
        Name <: Symbol,
        Arg <: ArgConf,
        Err <: ErrConf,
        Card <: Cardinality
    ] extends Set
  }

  /**
    * Computes the `Config.Set` given a collection of `Config` options. An
    * initial name must also be passed since no `Config.Set` can be unnamed.
    */
  trait FromConfig[N <: Symbol, C <: Config] {
    type Out <: Set
  }

  object FromConfig {

    type DefaultSetNamed[N <: Symbol] =
      Set.Of[N, NoArg, NoErr, Cardinality.One]

    type Aux[N <: Symbol, C <: Config, Out0 <: Set] =
      FromConfig[N, C] { type Out = Out0 }

    implicit def _FromConfig[N <: Symbol, C <: Config, Out0 <: Set](
        implicit _go: Go.Aux[DefaultSetNamed[N], C, Out0]
    ): Aux[N, C, Out0] = new FromConfig[N, C] { type Out = Out0 }

    /**
      * A type `Go[Set, C]#Result` is `Set` having had its various
      * parameters updated by the option `C`.
      */
    trait Go[S <: Set, C <: Config] {
      type Out <: Set
    }

    object Go {

      type Aux[S <: Set, C <: Config, Out0 <: Set] =
        Go[S, C] { type Out = Out0 }

      // identity
      implicit def _ConfigNil[N <: Symbol,
                              Ac <: ArgConf,
                              Ec <: ErrConf,
                              C <: Cardinality]
        : Aux[Set.Of[N, Ac, Ec, C], ConfigNil, Set.Of[N, Ac, Ec, C]] =
        new Go[Set.Of[N, Ac, Ec, C], ConfigNil] {
          type Out = Set.Of[N, Ac, Ec, C]
        }

      // "then": applies the right arg then the left arg thus letting
      // "lefty" results win
      implicit def _ConfigAnd[S <: Set,
                              Cl <: Config,
                              Cr <: Config,
                              Out0 <: Set,
                              Out1 <: Set](
          implicit rightResult: Aux[S, Cr, Out0],
          leftResult: Aux[Out0, Cl, Out1]
      ): Aux[S, Cl & Cr, Out1] =
        new Go[S, Cl & Cr] {
          type Out = Out1
        }

      // single "slot" updaters ...

      implicit def _Named[N0 <: Symbol,
                          Ac <: ArgConf,
                          Ec <: ErrConf,
                          C <: Cardinality,
                          N <: Symbol]
        : Aux[Set.Of[N0, Ac, Ec, C], Named[N], Set.Of[N, Ac, Ec, C]] =
        new Go[Set.Of[N0, Ac, Ec, C], Named[N]] {
          type Out = Set.Of[N, Ac, Ec, C]
        }

      implicit def _TakesArg[N <: Symbol,
                             Ac <: ArgConf,
                             Ec <: ErrConf,
                             C <: Cardinality,
                             A]
        : Aux[Set.Of[N, Ac, Ec, C], TakesArg[A], Set.Of[N, HasArg[A], Ec, C]] =
        new Go[Set.Of[N, Ac, Ec, C], TakesArg[A]] {
          type Out = Set.Of[N, HasArg[A], Ec, C]
        }

      implicit def _GivesErr[N <: Symbol,
                             Ac <: ArgConf,
                             Ec <: ErrConf,
                             C <: Cardinality,
                             E]
        : Aux[Set.Of[N, Ac, Ec, C], GivesErr[E], Set.Of[N, Ac, HasErr[E], C]] =
        new Go[Set.Of[N, Ac, Ec, C], GivesErr[E]] {
          type Out = Set.Of[N, Ac, HasErr[E], C]
        }

      implicit def _HasCard[N <: Symbol,
                            Ac <: ArgConf,
                            Ec <: ErrConf,
                            C0 <: Cardinality,
                            C <: Cardinality]
        : Aux[Set.Of[N, Ac, Ec, C0], HasCardinality[C], Set.Of[N, Ac, Ec, C]] =
        new Go[Set.Of[N, Ac, Ec, C0], HasCardinality[C]] {
          type Out = Set.Of[N, Ac, Ec, C]
        }

    }

  }

}
