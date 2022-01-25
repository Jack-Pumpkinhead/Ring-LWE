package math.martix

import com.ionspin.kotlin.bignum.integer.toBigInteger
import cryptography.lattice.ring_lwe.coding.*
import math.abstract_structure.instance.ringBigInteger
import math.abstract_structure.instance.ringUInt
import math.andPrint
import math.coding.permutation.SwitchIndexPermutation
import math.isPairwiseCoprimeUInt
import math.measureTimeAndPrint
import math.operation.multiply
import math.operation.product
import math.random.randomUIntMatrix
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.random.Random
import kotlin.random.nextUInt

/**
 * Created by CowardlyLion at 2022/1/16 22:16
 */
internal class PermutationMatrixTest {

    @Test
    fun timesImpl() {
        for (length in 2..5) {
            repeat(10) {
                val bound = List(length) { Random.nextUInt(2u..10u) }
                if (bound.isPairwiseCoprimeUInt()) {
                    println("bound: ${bound.joinToString(", ", "[", "]")}")
                    if (ringBigInteger.product(bound.map { it.toBigInteger() }) >= Int.MAX_VALUE.toBigInteger()) {
                        println("too big, skip.")
                    } else {

                        fun testPermutation(permutation: SwitchIndexPermutation, name: String) {
                            measureTimeAndPrint("perm $name") {
                                val m = PermutationMatrix(ringUInt, permutation).andPrint()
                                repeat(10) {

                                    val x = Random.randomUIntMatrix(permutation.size, 3u, 0u..10u).andPrint()
                                    val mx = (m * x).andPrint()
                                    val mx1 = multiply(m, x)
                                    assertEquals(mx, mx1)
                                }
                            }
                        }

                        testPermutation(permCRInv(bound), "CR")
                        testPermutation(permRCInv(bound), "RC")
                        testPermutation(permRLInv(bound), "RL")
                        testPermutation(permLRInv(bound), "LR")
                        testPermutation(permCLInv(bound), "CL")
                        testPermutation(permLCInv(bound), "LC")

                    }
                }
            }
        }
    }

    @Test
    fun timesImpl1() {
        val bound = listOf(8u, 9u, 5u)
        println("bound: ${bound.joinToString(", ", "[", "]")}")
        fun testPermutation(permutation: SwitchIndexPermutation, name: String) {
            println("perm $name")
            val m = PermutationMatrix(ringUInt, permutation).andPrint()
            repeat(10) {
//                        println("x$it")
                val x = Random.randomUIntMatrix(permutation.size, 3u, 0u..10u).andPrint()
//                        println("x$it a")
                val mx = (m * x).andPrint()
//                        println("x$it b")
                val mx1 = multiply(m, x)
//                        println("x$it c")
                assertEquals(mx, mx1)
//                        println("x$it d")
            }
        }

        testPermutation(permCRInv(bound), "CR")
        testPermutation(permRCInv(bound), "RC")
        testPermutation(permRLInv(bound), "RL")
        testPermutation(permLRInv(bound), "LR")
        testPermutation(permCLInv(bound), "CL")
        testPermutation(permLCInv(bound), "LC")

    }

    @Test
    fun timesRowParallelImpl() {
    }

    @Test
    fun multiplyToImpl() {
    }

    @Test
    fun multiplyToRowParallelImpl() {
    }
}