package math.integer

import kotlinx.coroutines.runBlocking
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

}