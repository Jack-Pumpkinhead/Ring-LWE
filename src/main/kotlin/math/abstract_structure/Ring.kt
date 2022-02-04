package math.abstract_structure

import util.stdlib.lazyAssert2
import util.stdlib.repeat

/**
 * Created by CowardlyLion at 2022/1/7 19:59
 *
 * assuming commutativity of multiplication for convenience
 */
interface Ring<A> : AddGroup<A>, Monoid<A> {

    fun ofInteger(a: UInt): A {
        lazyAssert2 {
            println("warning: slow implementation of ofInteger()")
        }
        var b = zero
        repeat(a) {
            b = add(b, one)
        }
        return b
    }

    fun ofInteger(a: Int): A {
        lazyAssert2 {
            println("warning: slow implementation of ofInteger()")
        }
        var b = zero
        if (a >= 0) {
            repeat(a) {
                b = add(b, one)
            }
        } else {
            repeat(-a) {
                b = subtract(b, one)
            }
        }
        return b
    }


}