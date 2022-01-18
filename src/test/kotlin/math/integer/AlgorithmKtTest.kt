package math.integer

import kotlinx.coroutines.runBlocking
import math.cache.primeOf
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test

/**
 * Created by CowardlyLion at 2022/1/5 20:30
 */
internal class AlgorithmKtTest {

    /**
     * total 664579 primes in 1..10000000
     * BUILD SUCCESSFUL in 1m 38s
     */
    @Test
    fun isPrime() {
        runBlocking {
            val bound = 10000000uL
            var index = 0
            var count = 0
            for (i in 1uL..bound) {
                if (i.isPrime()) {
                    assertEquals(primeOf(index++), i)
//                    println("$i is prime")
                    count++
                }
            }
            println("total $count primes in 1..$bound")
        }
    }

    @Test
    fun primeFactorization() {
        runBlocking {
            for (i in 1uL..10000uL) {
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
            for (i in ULong.MAX_VALUE downTo ULong.MAX_VALUE - 0uL) {
                println("$i = ${i.positivePrimeFactorization()}")
            }
        }
    }


    @Test
    fun prime() {
        runBlocking {
            for (i in 1uL..10000uL) {
                println("$i:  ${i.eulerTotient()}, \t ${i.radical()}, \t ${i.positivePrimeFactorization()}")
            }
        }
    }

    @Test
    fun extendedGCDTest() {
        for (a in 1u..100u) {
            for (b in 1u..100u) {
                val (r, s, gcd) = extendedGCD(a, b)
                assertEquals(a.mod(gcd), 0u)
                assertEquals(b.mod(gcd), 0u)
                assertEquals(r * a.toLong() + s * b.toLong(), gcd.toLong())
                println("$r*$a + $s*$b = $gcd")
            }
        }
    }

    @Test
    fun modInverse() {
        for (a in 1u..100u) {
            for (b in 2u..100u) {
                val aInv = a.modInverseOrNull(b)
                if (aInv == null) {
                    val (r, s, gcd) = extendedGCD(a, b)
                    assertNotEquals(gcd, 1u)
                    println("\t\t\t\t$r*$a + $s*$b = $gcd")
                } else {
                    assertEquals((a * aInv).mod(b), 1u)
                    println("$a*$aInv = 1 mod $b")
                }
            }
        }
    }


}