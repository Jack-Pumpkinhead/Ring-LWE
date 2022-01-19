package math.martix

import com.ionspin.kotlin.bignum.integer.toBigInteger
import math.abstract_structure.instance.ringBigInteger
import math.abstract_structure.instance.ringUInt
import math.andPrint
import math.coding.*
import math.isPairwiseCoprimeBigInteger
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
                val bound = List(length) { Random.nextUInt(2u..10u).toBigInteger() }
                if (bound.isPairwiseCoprimeBigInteger()) {
                    println("bound: ${bound.joinToString(", ", "[", "]")}")
                    if (ringBigInteger.product(bound) < Int.MAX_VALUE.toBigInteger()) {

                        fun testPermutation(permutation: SwitchIndexPermutation, name: String) {
                            println("perm $name")
                            val m = PermutationMatrix(ringUInt, permutation).andPrint()
                            repeat(10) {
                                val x = Random.randomUIntMatrix(permutation.size.uintValue(), permutation.size.uintValue(), 0u..10u).andPrint()
                                val mx = (m * x).andPrint()
                                val mx1 = multiply(m, x)
                                assertEquals(mx, mx1)
                            }
                        }

                        testPermutation(permCRInv(bound), "CR")
                        testPermutation(permRCInv(bound), "RC")
                        testPermutation(permRLInv(bound), "RL")
                        testPermutation(permLRInv(bound), "LR")
                        testPermutation(permCLInv(bound), "CL")
                        testPermutation(permLCInv(bound), "LC")

                    } else {
                        println("too big, skip.")
                    }
                }
            }
        }
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