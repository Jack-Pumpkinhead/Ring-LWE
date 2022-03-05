package math.abstract_structure.instance

import cryptography.lattice.ring_lwe.ring.root.RootCalculatorUnsafeComplexNumber
import kotlinx.coroutines.runBlocking
import math.abstract_structure.algorithm.power
import math.abstract_structure.algorithm.powerM
import math.abstract_structure.algorithm.powerS
import math.integer.uint.factored.primeFactorization
import org.junit.jupiter.api.Test

/**
 * Created by CowardlyLion at 2022/2/3 22:58
 */
internal class FieldComplexNumberDoubleTest {

    @Test
    fun testExponentOfRoot() {
        runBlocking {
            for (i in 2u..100u) {
                val root = RootCalculatorUnsafeComplexNumber.compute(1u, i.primeFactorization())
                println("${i}-th root: $root")
                println("power: ${root.ring.power(root.root.value, i)}")
                println("powerM: ${root.ring.powerM(root.root.value, i)}")
                println("powerS: ${root.ring.powerS(root.root.value, i)}")
            }
        }
    }

    @Test
    fun testLargeExponentOfRoot() {
        runBlocking {
            for (i in 1000000u..1000050u) {
                val root = RootCalculatorUnsafeComplexNumber.compute(1u, i.primeFactorization())
                println("${i}-th root: $root")
                println("power: ${root.ring.power(root.root.value, i)}")
                println("powerM: ${root.ring.powerM(root.root.value, i)}")
                println("powerS: ${root.ring.powerS(root.root.value, i)}")
            }
        }
    }

}