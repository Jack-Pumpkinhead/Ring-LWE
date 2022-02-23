package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.complex_double

import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.PrimeDftMatrix
import cryptography.lattice.ring_lwe.ring.RootUIntP
import cryptography.lattice.ring_lwe.ring.RootUIntPP
import cryptography.lattice.ring_lwe.ring.RootUIntPPP
import cryptography.lattice.ring_lwe.ring.RootUIntPPPI
import kotlinx.coroutines.runBlocking
import math.abstract_structure.instance.FieldComplexNumberDouble
import math.andPrint
import math.complex_number.ComplexNumber
import math.complex_number.maxRoundingError
import math.integer.uint.factored.primeFactorization
import math.martix.AbstractMatrix
import math.operation.matrixApproximatelyEquals
import math.random.randomMatrix
import math.statistic.TaskTimingStatistic
import math.timing.EqualTaskTiming
import math.timing.Task
import org.junit.jupiter.api.Test

/**
 * Created by CowardlyLion at 2022/2/23 22:37
 */
internal class PrimeDftMatrixComplexDoubleTest {

    data class TwoDftAndX(val ordinary: AbstractMatrix<ComplexNumber<Double>>, val fast: AbstractMatrix<ComplexNumber<Double>>, val x: AbstractMatrix<ComplexNumber<Double>>)

    @Test
    fun primeSpeed() {

        val map = mutableMapOf<UInt, TaskTimingStatistic<TwoDftAndX, AbstractMatrix<ComplexNumber<Double>>>>()
        val tasks = object : EqualTaskTiming<TwoDftAndX, AbstractMatrix<ComplexNumber<Double>>> {
            override fun equals(a: AbstractMatrix<ComplexNumber<Double>>, b: AbstractMatrix<ComplexNumber<Double>>): Boolean = matrixApproximatelyEquals(a, b)

            override val tasks: List<Task<TwoDftAndX, AbstractMatrix<ComplexNumber<Double>>>> = listOf(
                Task("ordinary") { (ordinary, fast, x) -> (ordinary * x).andPrint("ordi") },
                Task("    fast") { (ordinary, fast, x) -> (fast * x).andPrint("fast") }
            )
        }

        fun getStatistic(prime: UInt) = map.computeIfAbsent(prime) { TaskTimingStatistic(tasks) }

        suspend fun test(root: RootUIntPPPI<ComplexNumber<Double>>) {
//            println()
//            println("prime: ${primeField.prime}")
            when (root) {
                is RootUIntPPP -> {
                    for (i in 0u until root.order.factors.size.toUInt()) {
                        val primeRoot = root.primeSubrootAt(i)
                        val dftOrdinary = PrimeDftMatrix(primeRoot)
                        val dftFast = PrimeDftMatrixComplexDouble(primeRoot)
                        val statistic = getStatistic(primeRoot.order.prime)
                        repeat(2) {
                            val x = FieldComplexNumberDouble.randomMatrix(dftFast.columns, 2u, 100.0)
                            statistic.go(TwoDftAndX(dftOrdinary, dftFast, x))
                        }
                    }
                }
                is RootUIntPP  -> {
                    val primeRoot = root.primeSubroot()
                    val dftOrdinary = PrimeDftMatrix(primeRoot)
                    val dftFast = PrimeDftMatrixComplexDouble(primeRoot)
                    val statistic = getStatistic(primeRoot.order.prime)
                    repeat(2) {
                        val x = FieldComplexNumberDouble.randomMatrix(dftFast.columns, 2u, 100.0)
                        statistic.go(TwoDftAndX(dftOrdinary, dftFast, x))
                    }
                }
                is RootUIntP   -> {
                    val dftOrdinary = PrimeDftMatrix(root)
                    val dftFast = PrimeDftMatrixComplexDouble(root)
                    val statistic = getStatistic(root.order.prime)
                    repeat(2) {
                        val x = FieldComplexNumberDouble.randomMatrix(dftFast.columns, 2u, 100.0)
                        statistic.go(TwoDftAndX(dftOrdinary, dftFast, x))
                    }
                }
                else           -> error("unknown type of root $root, class: ${root::class}")
            }
        }

        runBlocking {
            for (i in 2u..500u) {
                repeat(3) {
                    test(FieldComplexNumberDouble.root(i.primeFactorization()))
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