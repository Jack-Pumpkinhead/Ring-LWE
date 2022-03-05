package util.test

import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.DftMatrixBuilder
import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.DftMatrixP
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
 * Created by CowardlyLion at 2022/3/5 13:28
 */
class ComplexFieldDftPTestBase(val builder: DftMatrixBuilder<ComplexNumber<Double>>, val statistic: TaskNearMatrixComplexDoubleStatistic<TwoMatrix<ComplexNumber<Double>>> = TaskNearMatrixComplexDoubleStatistic(TwoMatrixMultiplicationTiming())) {

    suspend fun test(range: UIntRange, repeat: UInt = 1u) {
        for (i in range) {
            val prime = primeOf(i)
            val root = RootCalculatorUnsafeComplexNumber.compute(1u, (prime.toUInt() - 1u).primeFactorization())
            val dft: DftMatrixP<ComplexNumber<Double>> = when (root) {
                is RootPPP -> builder.compute(SubrootCalculatorUnsafeComplexDouble.compute(root, root.order.factors.last().prime()))
                is RootPP  -> builder.compute(SubrootCalculatorUnsafeComplexDouble.subrootPrime(root))
                is RootP   -> builder.compute(root)
                else       -> errorUnknownObject(root)
            }
            util.stdlib.repeat(repeat) {
                val x = FieldComplexNumberDouble.randomMatrix(dft.columns, 2u, prime.toDouble())
                statistic.go(TwoMatrix(dft, x))
            }
        }
        statistic.printAverageAndStandardDeviation()
        statistic.printAverageAndMaxDistance()
        println("range: $range")
    }
}