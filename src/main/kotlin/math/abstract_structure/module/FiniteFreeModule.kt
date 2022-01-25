package math.abstract_structure.module

import math.abstract_structure.AddGroup

/**
 * Created by CowardlyLion at 2022/1/9 13:09
 *
 * TODO add description of all mathematical object (definition, etc)
 */
interface FiniteFreeModule<R, A> : AddGroup<A> {

//    val ring: CRing<R>    //should acted by a ring

    val dimension: UInt
    fun action(r: R, a: A): A

}