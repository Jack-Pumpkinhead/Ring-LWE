package math

import kotlinx.coroutines.runBlocking
import math.cache.primeOf
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

/**
 * Created by CowardlyLion at 2022/1/5 20:30
 */
internal class AlgorithmKtTest {

    @Test
    fun isPrime() {
        runBlocking {
            var index = 0
            for (i in -1L..100000L) {
                if (i.isPrime()) {
                    assertEquals(primeOf(index++), i)
//                    println("$i is prime")
                }
            }
        }
    }

    @Test
    fun primeFactorization() {
        runBlocking {
            for (i in 1L..10000L) {
                println("$i = ${i.positivePrimeFactorization()}")
            }
        }
    }

    /**
     * large number may take hours to compute and crashed by out-of-memory.
     * OutOfMemoryError after next chunk at primeOf(11778517) = 213393179
     * */
    @Test
    fun largePrimeFactorization() {
        runBlocking {
            for (i in Long.MAX_VALUE downTo Long.MAX_VALUE - 0L) {
                println("$i = ${i.positivePrimeFactorization()}")
            }
        }
    }


    @Test
    fun prime() {
        runBlocking {
            for (i in 1L..10000L) {
                println("$i:  ${i.eulerTotient()}, \t ${i.radical()}, \t ${i.positivePrimeFactorization()}")
            }
        }
    }
}