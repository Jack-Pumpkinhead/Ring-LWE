package cryptography.lattice.ring_lwe.matrix.canonical_embedding.complex_double

import cryptography.lattice.ring_lwe.ring.RootUIntPPI
import cryptography.lattice.ring_lwe.ring.RootUIntPPP
import kotlinx.coroutines.runBlocking
import math.complex_number.FieldComplexNumberDouble
import math.integer.uint.factored.primeFactorization
import math.integer.ulong.primeOf
import math.operation.maxAbsoluteDistance
import math.operation.multiply
import math.random.randomMatrix
import math.statistic.TaskNearMatrixComplexDoubleStatistic
import math.timing.TwoMatrix
import math.timing.TwoMatrixMultiplicationTiming
import org.junit.jupiter.api.Test

/**
 * Created by CowardlyLion at 2022/2/24 17:41
 */
internal class CeMatrixPPIBuilderComplexDoubleTest {

    //0.0
    //0.0
    //1.4865935071520193E-6
    @Test
    fun approximatelyEquals() {
        runBlocking {
            val prime = primeOf(1000u).toUInt()
            println("prime: $prime")
            val root = FieldComplexNumberDouble.root((prime - 1u).primeFactorization())
            when (root) {
                is RootUIntPPP -> {
                    for (subroot in root.allMaximalPrimePowerSubroot()) {
                        val ce = CeMatrixPPIBuilderComplexDouble.build(subroot)
                        val x = FieldComplexNumberDouble.randomMatrix(ce.columns, 2u, 100.0)
                        val m1 = ce * x
                        val m2 = multiply(ce, x)
                        println(maxAbsoluteDistance(m1, m2))
                    }
                }
                is RootUIntPPI -> {
                    val ce = CeMatrixPPIBuilderComplexDouble.build(root)
                    val x = FieldComplexNumberDouble.randomMatrix(ce.columns, 2u, 100.0)
                    val m1 = ce * x
                    val m2 = multiply(ce, x)
                    println(maxAbsoluteDistance(m1, m2))
                }
                else           -> error("unknown type of $root, class: ${root::class}")
            }

        }
    }

    fun testBase(range: UIntRange) {
        runBlocking {
            val statistic = TaskNearMatrixComplexDoubleStatistic(TwoMatrixMultiplicationTiming())
            for (i in range) {
                val prime = primeOf(i).toUInt()
                println("prime: $prime")
                val root = FieldComplexNumberDouble.root((prime - 1u).primeFactorization())
                when (root) {
                    is RootUIntPPP -> {
                        for (subroot in root.allMaximalPrimePowerSubroot()) {
                            val ce = CeMatrixPPIBuilderComplexDouble.build(subroot)
                            val x = FieldComplexNumberDouble.randomMatrix(ce.columns, 2u, 100.0)
                            statistic.go(TwoMatrix(ce, x))
                        }
                    }
                    is RootUIntPPI -> {
                        val ce = CeMatrixPPIBuilderComplexDouble.build(root)
                        val x = FieldComplexNumberDouble.randomMatrix(ce.columns, 2u, 100.0)
                        statistic.go(TwoMatrix(ce, x))
                    }
                    else           -> error("unknown type of $root, class: ${root::class}")
                }
            }
            statistic.printAverageAndStandardDeviation()
            statistic.printAverageAndMaxDistance()
            println("range: $range")
        }
    }

    //slower than direct multiplication
    // *  : average 0.50068ms, deviation 2.10017ms
    // *p : average 0.63064ms, deviation 2.28408ms
    // * t: average 0.48775ms, deviation 2.13863ms
    // *pt: average 0.61288ms, deviation 2.29246ms
    //d*  : average 0.45165ms, deviation 2.06778ms
    //samples: 533, total time: 1.430357518s
    //average distance: 2.2455892818350866E-12, max distance: 2.894298094685667E-10
    //range: 1..200
    @Test
    fun multiplication() {
        testBase(1u..200u)
    }

    //faster than direct multiplication
    // *  : average 10.71465ms, deviation 30.00791ms
    // *p : average 10.79390ms, deviation 28.73084ms
    // * t: average 6.26202ms, deviation 17.20787ms
    // *pt: average 8.01049ms, deviation 21.02101ms
    //d*  : average 15.20397ms, deviation 48.74371ms
    //samples: 32, total time: 1.631521082s
    //average distance: 1.2560087458339898E-7, max distance: 1.634744499325291E-6
    //range: 410..420
    @Test
    fun multiplicationLarge() {
        testBase(410u..420u)
    }


}