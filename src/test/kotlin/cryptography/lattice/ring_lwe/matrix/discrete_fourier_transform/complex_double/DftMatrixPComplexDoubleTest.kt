package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.complex_double

import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.default_impl.DftMatrixBuilderImpl
import cryptography.lattice.ring_lwe.ring.RootP
import cryptography.lattice.ring_lwe.ring.RootPP
import cryptography.lattice.ring_lwe.ring.RootPPP
import cryptography.lattice.ring_lwe.ring.RootPPPI
import cryptography.lattice.ring_lwe.ring.root.RootCalculatorUnsafeComplexNumber
import cryptography.lattice.ring_lwe.ring.subroot.SubrootCalculatorUnsafeComplexDouble
import kotlinx.coroutines.runBlocking
import math.andPrint
import math.complex_number.ComplexNumber
import math.complex_number.FieldComplexNumberDouble
import math.complex_number.maxRoundingError
import math.integer.uint.factored.primeFactorization
import math.martix.AbstractMatrix
import math.operation.matrixApproximatelyEquals
import math.random.randomMatrix
import math.statistic.TaskTimingStatistic
import math.timing.EqualTaskTiming
import math.timing.Task
import org.junit.jupiter.api.Test
import util.test.ComplexFieldDftPTestBase

/**
 * Created by CowardlyLion at 2022/2/23 22:37
 */
internal class DftMatrixPComplexDoubleTest {

    //a bit faster, more accurate and time-stable
    // *  : average 4.12152ms, deviation 11.23048ms
    // *p : average 4.96298ms, deviation 13.17360ms
    // * t: average 4.04469ms, deviation 11.02059ms
    // *pt: average 4.87464ms, deviation 13.04846ms
    //d*  : average 7.68429ms, deviation 27.55098ms
    //samples: 484, total time: 12.433056100s
    //average distance: 4.1359921383478954E-11, max distance: 1.0549887642492458E-9
    //range: 1..484
    @Test
    fun primeField() = runBlocking {
        ComplexFieldDftPTestBase(DftMatrixBuilderComplexDouble).test(1u..484u)
    }

    //a bit faster, more accurate and time-stable
    // *  : average 10.80775ms, deviation 20.23595ms
    // *p : average 12.88711ms, deviation 23.32326ms
    // * t: average 9.74449ms, deviation 18.34447ms
    // *pt: average 12.30071ms, deviation 22.30794ms
    //d*  : average 23.23105ms, deviation 54.61486ms
    //samples: 85, total time: 5.862544100s
    //average distance: 1.2860665405037597E-10, max distance: 1.0397239051466022E-9
    //range: 400..484
    @Test
    fun largePrimeField2() = runBlocking {
        ComplexFieldDftPTestBase(DftMatrixBuilderComplexDouble).test(400u..484u)
    }

    data class TwoDftAndX(val ordinary: AbstractMatrix<ComplexNumber<Double>>, val fast: AbstractMatrix<ComplexNumber<Double>>, val x: AbstractMatrix<ComplexNumber<Double>>)

    @Test
    fun primeSpeed() {

        val builderDefault = DftMatrixBuilderImpl<ComplexNumber<Double>>()
        val builderFast = DftMatrixBuilderComplexDouble

        val map = mutableMapOf<UInt, TaskTimingStatistic<TwoDftAndX, AbstractMatrix<ComplexNumber<Double>>>>()
        val tasks = object : EqualTaskTiming<TwoDftAndX, AbstractMatrix<ComplexNumber<Double>>> {
            override fun equals(a: AbstractMatrix<ComplexNumber<Double>>, b: AbstractMatrix<ComplexNumber<Double>>): Boolean = matrixApproximatelyEquals(a, b)

            override val tasks: List<Task<TwoDftAndX, AbstractMatrix<ComplexNumber<Double>>>> = listOf(
                Task("default") { (default, _, x) -> (default * x).andPrint("default") },
                Task("   fast") { (_, fast, x) -> (fast * x).andPrint("fast") }
            )
        }

        fun getStatistic(prime: UInt) = map.computeIfAbsent(prime) { TaskTimingStatistic(tasks) }

        suspend fun test(root: RootPPPI<ComplexNumber<Double>>) {
//            println()
//            println("prime: ${primeField.prime}")
            when (root) {
                is RootPPP -> {
                    val factors = root.order.factors
                    for (i in 0u until factors.size.toUInt()) {
                        val primeRoot = SubrootCalculatorUnsafeComplexDouble.compute(root, factors[i.toInt()].prime())
                        val dftDefault = builderDefault.compute(primeRoot)
                        val dftFast = builderFast.compute(primeRoot)
                        val statistic = getStatistic(primeRoot.order.prime)
                        repeat(2) {
                            val x = FieldComplexNumberDouble.randomMatrix(dftFast.columns, 2u, 100.0)
                            statistic.go(TwoDftAndX(dftDefault, dftFast, x))
                        }
                    }
                }
                is RootPP  -> {
                    val primeRoot = SubrootCalculatorUnsafeComplexDouble.subrootPrime(root)
                    val dftDefault = builderDefault.compute(primeRoot)
                    val dftFast = builderFast.compute(primeRoot)
                    val statistic = getStatistic(primeRoot.order.prime)
                    repeat(2) {
                        val x = FieldComplexNumberDouble.randomMatrix(dftFast.columns, 2u, 100.0)
                        statistic.go(TwoDftAndX(dftDefault, dftFast, x))
                    }
                }
                is RootP   -> {
                    val dftDefault = builderDefault.compute(root)
                    val dftFast = builderFast.compute(root)
                    val statistic = getStatistic(root.order.prime)
                    repeat(2) {
                        val x = FieldComplexNumberDouble.randomMatrix(dftFast.columns, 2u, 100.0)
                        statistic.go(TwoDftAndX(dftDefault, dftFast, x))
                    }
                }
                else       -> error("unknown type of root $root, class: ${root::class}")
            }
        }

        runBlocking {
            for (i in 2u..500u) {
                repeat(3) {
                    test(RootCalculatorUnsafeComplexNumber.compute(1u, i.primeFactorization()))
                }
            }
            /*for (i in 1u..150u) {
                repeat(3) {
                    test(FieldComplexNumberDouble.root(UIntP(primeOf(i).toUInt())))
                }
            }*/
            for ((key, statistic) in map.entries.sortedBy { it.key }) {
                println("prime: $key")
                statistic.printAverageAndStandardDeviation()
                println()
            }
            println("max error: $maxRoundingError")
        }
    }


}