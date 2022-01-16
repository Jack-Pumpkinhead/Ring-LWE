package math.coding

import com.ionspin.kotlin.bignum.integer.toBigInteger
import math.isPairwiseCoprimeBigInteger
import org.junit.jupiter.api.Test
import kotlin.random.Random
import kotlin.random.nextUInt

/**
 * Created by CowardlyLion at 2022/1/16 20:11
 */
internal class PermutationTest {

    @Test
    fun cycleDecomposition() {
        for (length in 2..5) {
            repeat(10) {
                val bound = List(length) { Random.nextUInt(2u..15u).toBigInteger() }
                if (bound.isPairwiseCoprimeBigInteger()) {
                    println("bound: ${bound.joinToString(", ", "[", "]")}")

                    fun printCycles(permutation: SwitchIndexPermutation, name: String) {
                        println("$name, ${if (permutation.isOddPermutation) "odd" else "even"}")
                        println(permutation.cycleDecomposition.joinToString(" ") { cycle -> cycle.joinToString(" ", "(", ")") })
                    }
                    printCycles(permCRInv(bound), "CR")
                    printCycles(permRCInv(bound), "RC")
                    printCycles(permRLInv(bound), "RL")
                    printCycles(permLRInv(bound), "LR")
                    printCycles(permCLInv(bound), "CL")
                    printCycles(permLCInv(bound), "LC")
                }
            }
        }
    }

}