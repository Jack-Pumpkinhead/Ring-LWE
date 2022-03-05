package util.test

import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.DftMatrixBuilder
import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.DftMatrixP
import cryptography.lattice.ring_lwe.ring.RootP
import cryptography.lattice.ring_lwe.ring.RootPP
import cryptography.lattice.ring_lwe.ring.RootPPP
import cryptography.lattice.ring_lwe.ring.subroot.SubrootCalculatorUnsafeModularUInt
import math.integer.uint.factored.UIntP
import math.integer.uint.modular.ModularUInt
import math.integer.ulong.primeOf
import math.martix.AbstractMatrix
import math.random.randomMatrix
import math.statistic.TaskTimingStatistic
import math.timing.EqualTwoMatrixMultiplicationTiming
import math.timing.TwoMatrix
import util.errorUnknownObject

/**
 * Created by CowardlyLion at 2022/3/5 12:25
 */
class PrimeFieldDftPTestBase(val builder: DftMatrixBuilder<ModularUInt>, val statistic: TaskTimingStatistic<TwoMatrix<ModularUInt>, AbstractMatrix<ModularUInt>> = TaskTimingStatistic(EqualTwoMatrixMultiplicationTiming())) {

    suspend fun test(range: UIntRange, repeat: UInt = 1u) {
        for (i in range) {
            val primeField = UIntP(primeOf(i).toUInt()).primeField
            val root = primeField.firstGenerator
            val dft: DftMatrixP<ModularUInt> = when (root) {
                is RootPPP -> builder.compute(SubrootCalculatorUnsafeModularUInt.compute(root, root.order.factors.last().prime()))
                is RootPP  -> builder.compute(SubrootCalculatorUnsafeModularUInt.subrootPrime(root))
                is RootP   -> builder.compute(root)
                else       -> errorUnknownObject(root)
            }
            util.stdlib.repeat(repeat) {
                val x = primeField.randomMatrix(dft.columns, 2u)
                statistic.go(TwoMatrix(dft, x))
            }
        }
        statistic.printAverageAndStandardDeviation()
        println("range: $range")
    }
}