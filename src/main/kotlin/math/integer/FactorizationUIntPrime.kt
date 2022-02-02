package math.integer

import kotlinx.coroutines.runBlocking
import math.abstract_structure.instance.FieldModularUInt
import util.stdlib.lazyAssert2

/**
 * Created by CowardlyLion at 2022/1/27 12:03
 */
class FactorizationUIntPrime(val value: UInt) {

    init {
        lazyAssert2 {
            runBlocking {
                assert(value.isPrime())
            }
        }
    }

    val primeField get() = FieldModularUInt(value)

    val eulerTotient: UInt get() = value - 1u
    val radical: UInt get() = value


}