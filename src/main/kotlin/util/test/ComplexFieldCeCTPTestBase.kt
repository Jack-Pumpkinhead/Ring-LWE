package util.test

import cryptography.lattice.ring_lwe.matrix.canonical_embedding_conjugate_transpose.CeCTMatrixBuilder
import cryptography.lattice.ring_lwe.ring.RootP
import cryptography.lattice.ring_lwe.ring.RootPP
import cryptography.lattice.ring_lwe.ring.RootPPP
import cryptography.lattice.ring_lwe.ring.root.RootCalculatorUnsafeComplexNumber
import cryptography.lattice.ring_lwe.ring.subroot.SubrootCalculatorUnsafeComplexDouble
import math.complex_number.ComplexNumber
import math.complex_number.FieldComplexNumberDouble
import math.integer.uint.factored.primeFactorization
import math.integer.ulong.primeOf
import math.random.randomMatrix
import math.statistic.TaskNearMatrixComplexDoubleStatistic
import math.timing.TwoMatrix
import math.timing.TwoMatrixMultiplicationTiming
import util.errorUnknownObject

/**
 * Created by CowardlyLion at 2022/3/5 14:10
 */
class ComplexFieldCeCTPTestBase(val builder: CeCTMatrixBuilder<ComplexNumber<Double>>, val statistic: TaskNearMatrixComplexDoubleStatistic<TwoMatrix<ComplexNumber<Double>>> = TaskNearMatrixComplexDoubleStatistic(TwoMatrixMultiplicationTiming())) {

//    val creator = FactorizationToRoot1ToCeCT(RootCalculatorUnsafeComplexNumber, builder)

    suspend fun test(range: UIntRange, repeat: UInt = 1u) {
        for (i in range) {
            val prime = primeOf(i).toUInt()
            val root = RootCalculatorUnsafeComplexNumber.compute(1u, (prime - 1u).primeFactorization())
            when (root) {
                is RootPPP -> {
                    for (factor in root.order.factors) {
                        val subroot = SubrootCalculatorUnsafeComplexDouble.compute(root, factor.prime())
                        val ce = builder.compute(subroot)
                        util.stdlib.repeat(repeat) {
                            val x = FieldComplexNumberDouble.randomMatrix(ce.columns, 2u, factor.prime.toDouble())
                            statistic.go(TwoMatrix(ce, x))
                        }
                    }
                }
                is RootPP  -> {
                    val ce = builder.compute(SubrootCalculatorUnsafeComplexDouble.subrootPrime(root))
                    util.stdlib.repeat(repeat) {
                        val x = FieldComplexNumberDouble.randomMatrix(ce.columns, 2u, root.order.prime.toDouble())
                        statistic.go(TwoMatrix(ce, x))
                    }
                }
                is RootP   -> {
                    val ce = builder.compute(root)
                    util.stdlib.repeat(repeat) {
                        val x = FieldComplexNumberDouble.randomMatrix(ce.columns, 2u, root.order.value.toDouble())
                        statistic.go(TwoMatrix(ce, x))
                    }
                }
                else       -> errorUnknownObject(root)
            }
        }
        statistic.printAverageAndStandardDeviation()
        statistic.printAverageAndMaxDistance()
        println("range: $range")
    }

}