package cryptography.lattice.ring_lwe.matrix.canonical_embedding.conjugate_transpose

import cryptography.lattice.ring_lwe.matrix.canonical_embedding.complex_double.CeMatrixPPIBuilderComplexDouble
import cryptography.lattice.ring_lwe.ring.RootUIntP
import cryptography.lattice.ring_lwe.ring.RootUIntPP
import cryptography.lattice.ring_lwe.ring.RootUIntPPP
import kotlinx.coroutines.runBlocking
import math.complex_number.FieldComplexNumberDouble
import math.integer.uint.factored.primeFactorization
import math.integer.ulong.primeOf
import math.random.randomMatrix
import math.statistic.TaskNearMatrixComplexDoubleStatistic
import math.timing.TwoMatrix
import math.timing.TwoMatrixMultiplicationTiming
import org.junit.jupiter.api.Test

/**
 * Created by CowardlyLion at 2022/2/24 19:46
 */
internal class PrimeCeCTMatrixComplexDoubleTest {

    fun testBase(range: UIntRange) {
        runBlocking {
            val statistic = TaskNearMatrixComplexDoubleStatistic(TwoMatrixMultiplicationTiming())
            for (i in range) {
                val prime = primeOf(i).toUInt()
                val root = FieldComplexNumberDouble.root((prime - 1u).primeFactorization())
                when (root) {
                    is RootUIntPPP -> {
                        for (factor in 0u until root.order.factors.size.toUInt()) {
                            val subroot = root.primeSubrootAt(factor)
                            val ce = CeMatrixPPIBuilderComplexDouble.build(subroot)
                            val x = FieldComplexNumberDouble.randomMatrix(ce.columns, 2u, 100.0)
                            statistic.go(TwoMatrix(ce, x))
                        }
                    }
                    is RootUIntPP  -> {
                        val ce = CeMatrixPPIBuilderComplexDouble.build(root.primeSubroot())
                        val x = FieldComplexNumberDouble.randomMatrix(ce.columns, 2u, 100.0)
                        statistic.go(TwoMatrix(ce, x))
                    }
                    is RootUIntP   -> {
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

    @Test
    fun multiplication() {
        testBase(1u..200u)
    }

    // *  : average 9.90177ms, deviation 27.91600ms
    // *p : average 10.02715ms, deviation 26.34258ms
    // * t: average 5.79781ms, deviation 15.77596ms
    // *pt: average 7.80328ms, deviation 20.87747ms
    //d*  : average 13.88524ms, deviation 44.27437ms
    //samples: 32, total time: 1.517288404s
    //average distance: 1.4031459823129978E-7, max distance: 1.9181513302452876E-6
    //range: 410..420
    @Test
    fun multiplicationLarge() {
        testBase(410u..420u)
    }

}