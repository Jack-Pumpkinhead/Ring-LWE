package math.abstract_structure.instance

import kotlinx.coroutines.runBlocking
import math.integer.primeFactorization
import math.abstract_structure.algorithm.power
import math.abstract_structure.algorithm.powerM
import math.abstract_structure.algorithm.powerS
import org.junit.jupiter.api.Test

/**
 * Created by CowardlyLion at 2022/2/3 22:58
 */
internal class FieldComplexNumberDoubleTest {

    @Test
    fun testExponentOfRoot() {
        runBlocking {
            for (i in 1u..100u) {
                val root = FieldComplexNumberDouble.root(i.primeFactorization())
                println("${i}-th root: $root")
                println("power: ${root.ring.power(root.root, i)}")
                println("powerM: ${root.ring.powerM(root.root, i)}")
                println("powerS: ${root.ring.powerS(root.root, i)}")
            }
        }
    }

    @Test
    fun testLargeExponentOfRoot() {
        runBlocking {
            for (i in 1000000u..1000050u) {
                val root = FieldComplexNumberDouble.root(i.primeFactorization())
                println("${i}-th root: $root")
                println("power: ${root.ring.power(root.root, i)}")
                println("powerM: ${root.ring.powerM(root.root, i)}")
                println("powerS: ${root.ring.powerS(root.root, i)}")
            }
        }
    }

}