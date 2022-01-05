package math

import kotlinx.coroutines.runBlocking
import math.cache.primeOf
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

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

    @Test
    fun prime() {
        runBlocking {
            for (i in 1L..10000L) {
                println("$i:  ${i.eulerTotient()}, \t ${i.radical()}, \t ${i.positivePrimeFactorization()}")
            }
        }
    }
}