package util.test

import cryptography.lattice.ring_lwe.matrix.canonical_embedding_conjugate_transpose.CeCTMatrixBuilder
import cryptography.lattice.ring_lwe.ring.RootPPI
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
 * Created by CowardlyLion at 2022/3/5 14:47
 */
class ComplexFieldCeCTPPITestBase(val builder: CeCTMatrixBuilder<ComplexNumber<Double>>, val statistic: TaskNearMatrixComplexDoubleStatistic<TwoMatrix<ComplexNumber<Double>>> = TaskNearMatrixComplexDoubleStatistic(TwoMatrixMultiplicationTiming())) {

    //    val creator = FactorizationToRoot1ToCeCT(RootCalculatorUnsafeComplexNumber, builder)

    suspend fun test(range: UIntRange, repeat: UInt = 1u) {
        for (i in range) {
            val prime = primeOf(i).toUInt()
            val root = RootCalculatorUnsafeComplexNumber.compute(1u, (prime - 1u).primeFactorization())
            when (root) {
                is RootPPP -> {
                    for (subroot in SubrootCalculatorUnsafeComplexDouble.allMaximalPrimePowerSubroot(root)) {
                        val ce_ct = builder.compute(subroot)
                        util.stdlib.repeat(repeat) {
                            val x = FieldComplexNumberDouble.randomMatrix(ce_ct.columns, 2u, subroot.order.value.toDouble())
                            statistic.go(TwoMatrix(ce_ct, x))
                        }
                    }
                }
                is RootPPI -> {
                    val ce_ct = builder.compute(root)
                    util.stdlib.repeat(repeat) {
                        val x = FieldComplexNumberDouble.randomMatrix(ce_ct.columns, 2u, root.order.value.toDouble())
                        statistic.go(TwoMatrix(ce_ct, x))
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