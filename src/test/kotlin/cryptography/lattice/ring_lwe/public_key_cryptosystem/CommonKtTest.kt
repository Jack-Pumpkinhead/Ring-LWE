package cryptography.lattice.ring_lwe.public_key_cryptosystem

import kotlinx.coroutines.runBlocking
import math.abstract_structure.instance.FieldDouble
import math.complex_number.maxRoundingError
import math.complex_number.roundToReal
import math.integer.long.RingLong
import math.integer.uint.factored.UIntPPI
import math.integer.uint.factored.primeFactorization
import math.map
import math.martix.columnVector
import org.junit.jupiter.api.Test
import util.stdlib.mutableList
import util.stdlib.toString
import kotlin.math.abs
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.roundToLong
import kotlin.random.Random
import kotlin.random.nextLong

/**
 * Created by CowardlyLion at 2022/2/24 22:53
 */
internal class CommonKtTest {

    @Test
    fun samplingContinuousGaussianToDecodingBasis() {
        runBlocking {
            for (i in 3u..1000u) {
                val order = i.primeFactorization()
                if (order is UIntPPI) {
                    println(order.eulerTotient.primeFactorization())
                    val x = Random.samplingContinuousGaussianToDecodingBasis(order, 0.0, 1.0)
                    val list = x.columnListAt(0u)
                    list.map { it.roundToReal() }
                    println(list.map { it.toString(20u) })
                    println("maxError: $maxRoundingError")
                }
            }
        }
    }

    @Test
    fun randomlyNearest() {
        runBlocking {

            val p = 53u

            for (i in 7u..7u) {
                val order = i.primeFactorization()
                if (order is UIntPPI) {
                    println("order: $order")
                    println("φ(order): ${order.eulerTotient.primeFactorization()}")
                    val c = RingLong.columnVector(order.eulerTotient) { it.toLong() }
                    val x = Random.samplingContinuousGaussianToDecodingBasis(order, 0.0, 1.0)
                    val realX = x.roundToReal()
                    println("x: ${x.columnListAt(0u)}")
                    println("realX: ${realX.columnListAt(0u)}")
                    println("maxError: $maxRoundingError")
                    repeat(100) {
                        val near0 = Random.randomlyNearestElement(realX, c.map(FieldDouble) { it.toDouble() }, p)
                        val near1 = Random.randomlyNearestElement1(realX, c, p)
                        println(near0.columnListAt(0u))
                        println(near1.columnListAt(0u))
                        println("maxError: $maxRoundingError")
                    }
                }
            }
        }
    }

    @Test
    fun randomlyNearestStatistic() {
        runBlocking {

            val p = 53u

            for (i in 7u..7u) {
                val order = i.primeFactorization()
                if (order is UIntPPI) {
                    println("order: $order")
                    println("φ(order): ${order.eulerTotient.primeFactorization()}")
                    val c = RingLong.columnVector(order.eulerTotient) { it.toLong() }   //can change c by multiple of p
                    val x = Random.samplingContinuousGaussianToDecodingBasis(order, 0.0, 1.0)
                    val realX = x.roundToReal()
                    val c_minus_x = c.map(FieldDouble) { it.toDouble() }.subtract(realX)
                    println("x: ${x.columnListAt(0u)}")
                    println("realX: ${realX.columnListAt(0u)}")
                    println("maxError: $maxRoundingError")
                    var total = 0u
                    val minus0 = mutableList<UInt?>(6u) { 0u }
                    val minus1 = mutableList<UInt?>(6u) { 0u }
                    val plus0 = mutableList<UInt?>(6u) { 0u }
                    val plus1 = mutableList<UInt?>(6u) { 0u }
                    var c1 = c.map(RingLong) { it + (Random.nextLong(-10L..10L) * p.toLong()) }
                    repeat(10000000) {
                        c1 = c1.map(RingLong) { it + (Random.nextLong(-10L..10L) * p.toLong()) }    //test if it is a 'valid' discretization method. i.e. irrelevant of representing elements in the coset.
                        val near0 = Random.randomlyNearestElement(realX, c1.map(FieldDouble) { it.toDouble() }, p)
                        val near1 = Random.randomlyNearestElement1(realX, c1, p)
                        val near0Integer = near0.map(RingLong) {
                            maxRoundingError = max(maxRoundingError, abs(it - it.roundToLong()))
                            it.roundToLong()
                        }
                        for (j in 0u until order.eulerTotient) {
                            if (c_minus_x.vectorElementAtUnsafe(j) > 0.0) {
                                when (val n0j = near0Integer.vectorElementAtUnsafe(j)) {
                                    j.toLong()              -> {}
                                    j.toLong() - p.toLong() -> {
                                        minus0[j.toInt()] = minus0[j.toInt()]!! + 1u
                                    }
                                    else                    -> error("wrong discretization: $n0j, j: $j")
                                }
                                when (val n1j = near1.vectorElementAtUnsafe(j)) {
                                    j.toLong()              -> {}
                                    j.toLong() - p.toLong() -> {
                                        minus1[j.toInt()] = minus1[j.toInt()]!! + 1u
                                    }
                                    else                    -> error("wrong discretization: $n1j, j: $j")
                                }
                                plus0[j.toInt()] = null
                                plus1[j.toInt()] = null
                            } else {
                                minus0[j.toInt()] = null
                                minus1[j.toInt()] = null
                                when (val n0j = near0Integer.vectorElementAtUnsafe(j)) {
                                    j.toLong()              -> {}
                                    j.toLong() + p.toLong() -> {
                                        plus0[j.toInt()] = plus0[j.toInt()]!! + 1u
                                    }
                                    else                    -> error("wrong discretization: $n0j, j: $j")
                                }
                                when (val n1j = near1.vectorElementAtUnsafe(j)) {
                                    j.toLong()              -> {}
                                    j.toLong() + p.toLong() -> {
                                        plus1[j.toInt()] = plus1[j.toInt()]!! + 1u
                                    }
                                    else                    -> error("wrong discretization: $n1j, j: $j")
                                }
                            }
                        }
                        total++
//                        println(near0.columnListAt(0u))
//                        println(near1.columnListAt(0u))
                    }
                    println("total: $total")
                    println("minus0: $minus0")
                    println("plus0 : $plus0")
                    println("minus1: $minus1")
                    println("plus1 : $plus1")
                    println("minus0/total: ${minus0.map { if (it == null) null else it.toDouble() / total.toDouble() }}")
                    println(" plus0/total: ${plus0.map { if (it == null) null else it.toDouble() / total.toDouble() }}")
                    println("minus1/total: ${minus1.map { if (it == null) null else it.toDouble() / total.toDouble() }}")
                    println(" plus1/total: ${plus1.map { if (it == null) null else it.toDouble() / total.toDouble() }}")
                    println("|(c-x)/p|: ${c_minus_x.columnListAt(0u).map { (it / p.toDouble()).absoluteValue }}")
                    println("maxError: $maxRoundingError")

                }
            }
        }
    }

    @Test
    fun findOrder() {
        runBlocking {
            for (i in 3u..120u) {
                val order = i.primeFactorization()
                if (order is UIntPPI) {
                    println("order: $order")
                    println("φ(order): ${order.eulerTotient.primeFactorization()}")
                }
            }
        }
    }

}