package util.test

import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.DftMatrixBuilder
import cryptography.lattice.ring_lwe.ring.root.RootCalculatorUnsafeComplexNumber
import math.complex_number.ComplexNumber
import math.complex_number.FieldComplexNumberDouble
import math.integer.uint.factored.primeFactorization
import math.integer.ulong.primeOf
import math.random.randomMatrix
import math.statistic.TaskNearMatrixComplexDoubleStatistic
import math.timing.TwoMatrix
import math.timing.TwoMatrixMultiplicationTiming
import util.mapper.FactorizationToRoot1ToDft

/**
 * Created by CowardlyLion at 2022/3/5 12:52
 */
class ComplexFieldDftTestBase(builder: DftMatrixBuilder<ComplexNumber<Double>>, val statistic: TaskNearMatrixComplexDoubleStatistic<TwoMatrix<ComplexNumber<Double>>> = TaskNearMatrixComplexDoubleStatistic(TwoMatrixMultiplicationTiming())) {

    val creator = FactorizationToRoot1ToDft(RootCalculatorUnsafeComplexNumber, builder)

    suspend fun test(range: UIntRange, repeat: UInt = 1u) {
        for (i in range) {
            val prime = primeOf(i).toUInt()
            val dft = creator.compute(1u, (prime - 1u).primeFactorization())
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