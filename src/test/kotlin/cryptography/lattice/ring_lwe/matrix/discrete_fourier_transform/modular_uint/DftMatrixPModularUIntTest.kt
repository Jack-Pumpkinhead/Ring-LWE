package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.modular_uint

import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.default_impl.DftMatrixBuilderImpl
import cryptography.lattice.ring_lwe.ring.RootP
import cryptography.lattice.ring_lwe.ring.RootPP
import cryptography.lattice.ring_lwe.ring.RootPPP
import cryptography.lattice.ring_lwe.ring.subroot.SubrootCalculatorUnsafeModularUInt
import kotlinx.coroutines.runBlocking
import math.andPrint
import math.complex_number.maxRoundingError
import math.integer.uint.factored.UIntP
import math.integer.uint.factored.primeFactorization
import math.integer.uint.modular.FieldModularUInt
import math.integer.uint.modular.ModularUInt
import math.integer.uint.nextTwoPositivePower
import math.integer.ulong.primeOf
import math.martix.AbstractMatrix
import math.operation.matrixEquals
import math.random.randomMatrix
import math.statistic.TaskTimingStatistic
import math.timing.EqualTaskTiming
import math.timing.Task
import org.junit.jupiter.api.Test
import util.errorUnknownObject
import util.test.PrimeFieldDftPTestBase

/**
 * Created by CowardlyLion at 2022/2/8 12:04
 */
internal class DftMatrixPModularUIntTest {

    //TODO add a inverse matrix test in another file

    //* faster than d*
    // *  : average 3.61650ms, deviation 13.09069ms
    // *p : average 4.23999ms, deviation 14.74509ms
    // * t: average 3.42437ms, deviation 12.08098ms
    // *pt: average 4.13913ms, deviation 14.29940ms
    //d*  : average 4.73365ms, deviation 16.64035ms
    //samples: 500, total time: 10.076820200s
    //range: 1..500
    @Test
    fun primeField() = runBlocking {
        PrimeFieldDftPTestBase(DftMatrixBuilderModularUInt).test(1u..500u)
    }

    //can perform large prime field DFT with Double precision FFT now
    //* slower than d*
    // *  : average 30.24150ms, deviation 59.26232ms
    // *p : average 30.85131ms, deviation 48.86046ms
    // * t: average 21.59097ms, deviation 28.90541ms
    // *pt: average 26.43380ms, deviation 37.58643ms
    //d*  : average 26.80458ms, deviation 44.42091ms
    //samples: 20, total time: 2.718443400s
    //range: 1500..1509
    @Test
    fun largePrimeField() = runBlocking {
        PrimeFieldDftPTestBase(DftMatrixBuilderModularUInt).test(1500u..1509u, 2u)
    }

    // *  : average 418.82400ms, deviation 165.93335ms
    // *p : average 444.88895ms, deviation 129.52874ms
    // * t: average 308.85035ms, deviation 12.64045ms
    // *pt: average 353.38000ms, deviation 2.17068ms
    //d*  : average 2045.04325ms, deviation 23.50642ms
    //samples: 2, total time: 7.141973100s
    //range: 1510..1510
    @Test
    fun largePrimeField1() = runBlocking {
        PrimeFieldDftPTestBase(DftMatrixBuilderModularUInt).test(1510u..1510u, 2u)
    }

    //situations that can avoid using padding method is very few
    @Test
    fun twoPower() {
        runBlocking {
            for (i in 2u..1000000u) {
                val prime = primeOf(i)
                val primeDec = prime.toUInt() - 1u
                val nextTwoPower = nextTwoPositivePower(primeDec)
                if (nextTwoPower.value == primeDec) {
                    println("$prime - 1 = ${nextTwoPower.prime}^${nextTwoPower.power}")
                }
            }
        }
    }


    data class TwoDftAndX(val ordinary: AbstractMatrix<ModularUInt>, val fast: AbstractMatrix<ModularUInt>, val x: AbstractMatrix<ModularUInt>)

    @Test
    fun primeSpeed() {

        val builderDefault = DftMatrixBuilderImpl<ModularUInt>()
        val builderFast = DftMatrixBuilderModularUInt

        val map = mutableMapOf<UInt, TaskTimingStatistic<TwoDftAndX, AbstractMatrix<ModularUInt>>>()
        val tasks = object : EqualTaskTiming<TwoDftAndX, AbstractMatrix<ModularUInt>> {
            override fun equals(a: AbstractMatrix<ModularUInt>, b: AbstractMatrix<ModularUInt>): Boolean = matrixEquals(a, b)

            override val tasks: List<Task<TwoDftAndX, AbstractMatrix<ModularUInt>>> = listOf(
                Task(" default") { (default, _, x) -> (default * x).andPrint("default") },
                Task("    fast") { (_, fast, x) -> (fast * x).andPrint("fast") }
            )
        }

        fun getStatistic(prime: UInt) = map.computeIfAbsent(prime) { TaskTimingStatistic(tasks) }

        suspend fun test(primeField: FieldModularUInt) {
//            println()
//            println("prime: ${primeField.prime}")
            val root = primeField.firstGenerator
            when (root) {
                is RootPPP -> {
                    for (i in 0u until root.order.factors.size.toUInt()) {
                        val primeRoot = SubrootCalculatorUnsafeModularUInt.compute(root, root.order.factors[i.toInt()].prime())
                        val dftDefault = builderDefault.compute(primeRoot)
                        val dftFast = builderFast.compute(primeRoot)
                        val statistic = getStatistic(primeRoot.order.value)
                        maxRoundingError = 0.0
                        repeat(2) {
                            val x = primeField.randomMatrix(dftFast.columns, 2u)
//                            println("o: ")
//                            println(matrixToString(dftOrdinary))
//                            println("f: ")
//                            println(matrixToString(dftFast))
//                            println("x: ")
//                            println(matrixToString(x))

                            statistic.go(TwoDftAndX(dftDefault, dftFast, x))
                        }
                        if (maxRoundingError > 0.0) {
                            println("prime: $primeRoot, maxRoundingError: $maxRoundingError")
                        }
                    }
                }
                is RootPP  -> {
                    val primeRoot = SubrootCalculatorUnsafeModularUInt.subrootPrime(root)
                    val dftDefault = builderDefault.compute(primeRoot)
                    val dftFast = builderFast.compute(primeRoot)
                    val statistic = getStatistic(primeRoot.order.prime)
                    maxRoundingError = 0.0
                    repeat(2) {
                        val x = primeField.randomMatrix(dftFast.columns, 2u)
                        statistic.go(TwoDftAndX(dftDefault, dftFast, x))
                    }
                    if (maxRoundingError > 0.0) {
                        println("prime: $primeRoot, maxRoundingError: $maxRoundingError")
                    }
                }
                is RootP   -> {
                    val dftDefault = builderDefault.compute(root)
                    val dftFast = builderFast.compute(root)
                    val statistic = getStatistic(root.order.prime)
                    maxRoundingError = 0.0
                    repeat(2) {
                        val x = primeField.randomMatrix(dftFast.columns, 2u)
                        statistic.go(TwoDftAndX(dftDefault, dftFast, x))
                    }
                    if (maxRoundingError > 0.0) {
                        println("prime: $root, maxRoundingError: $maxRoundingError")
                    }
                }
                else       -> errorUnknownObject(root)
            }
        }

        runBlocking {
            for (i in 1u..500u) {
                val primeField = UIntP(primeOf(i).toUInt()).primeField
                repeat(3) {
                    test(primeField)
                }
            }

            for ((key, statistic) in map.entries.sortedBy { it.key }) {
                println("prime: $key, primeDec: ${(key-1u).primeFactorization()}")
                statistic.printAverageAndStandardDeviation()
                println()
            }
        }
    }

}