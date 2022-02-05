package math.abstract_structure

import math.abstract_structure.algorithm.multipleM
import util.stdlib.lazyAssert2

/**
 * Created by CowardlyLion at 2022/1/7 19:59
 *
 * assuming commutativity of multiplication for convenience
 */
interface Ring<A> : AddGroup<A>, Monoid<A> {

    fun ofInteger(a: UInt): A {
        lazyAssert2 {
            println("warning: a little (time log(a)) slow implementation of ofInteger()")
        }
        return multipleM(one, a)
    }

    fun ofInteger(a: Int): A {
        lazyAssert2 {
            println("warning: a little (time log(a)) slow implementation of ofInteger()")
        }
        return multipleM(one, a)
    }

    fun ofInteger(a: ULong): A {
        lazyAssert2 {
            println("warning: a little (time log(a)) slow implementation of ofInteger()")
        }
        return multipleM(one, a)
    }

    fun ofInteger(a: Long): A {
        lazyAssert2 {
            println("warning: a little (time log(a)) slow implementation of ofInteger()")
        }
        return multipleM(one, a)
    }

}