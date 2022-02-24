package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.modular_uint

import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.PrimeDftMatrix
import cryptography.lattice.ring_lwe.ring.RootUIntP
import cryptography.lattice.ring_lwe.ring.RootUIntPP
import cryptography.lattice.ring_lwe.ring.RootUIntPPP
import kotlinx.coroutines.runBlocking
import math.andPrint
import math.integer.uint.factored.PrimeUInt
import math.integer.uint.modular.FieldModularUInt
import math.integer.uint.modular.ModularUInt
import math.integer.uint.nextTwoPositivePower
import math.integer.ulong.primeOf
import math.martix.AbstractMatrix
import math.operation.matrixEquals
import math.random.randomMatrix
import math.statistic.TaskTimingStatistic
import math.timing.EqualTaskTiming
import math.timing.EqualTwoMatrixMultiplicationTiming
import math.timing.Task
import math.timing.TwoMatrix
import org.junit.jupiter.api.Test

/**
 * Created by CowardlyLion at 2022/2/8 12:04
 */
internal class PrimeDftMatrixModularUIntTest {

    suspend fun testBase(range: UIntRange) {
        val statistic = TaskTimingStatistic(EqualTwoMatrixMultiplicationTiming<ModularUInt>())

        for (i in range) {
            val primeField = PrimeUInt(primeOf(i).toUInt()).primeField
            val root = primeField.firstGenerator
            val dft = when (root) {
                is RootUIntPPP -> PrimeDftMatrixModularUInt(root.primeSubrootAt(root.order.factors.size.toUInt() - 1u))
                is RootUIntPP  -> PrimeDftMatrixModularUInt(root.primeSubroot())
                is RootUIntP   -> PrimeDftMatrixModularUInt(root)
                else           -> error("unknown type of root $root, class: ${root::class}")
            }
            val x = primeField.randomMatrix(dft.columns, 2u)
            statistic.go(TwoMatrix(dft, x))
        }
        statistic.printAverageAndStandardDeviation()
        println("range: $range")
    }

    //outperform ordinary method now.
    // *  : average 3.30884ms, deviation 12.14227ms
    // *p : average 3.85853ms, deviation 12.75656ms
    // * t: average 2.98206ms, deviation 10.17931ms
    // *pt: average 3.65113ms, deviation 12.28026ms
    //d*  : average 4.96608ms, deviation 17.64326ms
    //samples: 500, total time: 9.383326200s
    //range: 1..500
    @Test
    fun primeField() = runBlocking {
        testBase(1u..500u)
    }

    /*//cannot perform large prime field DFT with Double precision FFT
    @Test
    fun largePrimeField() = runBlocking {
        testBase(1500u..1509u)
    }*/

   /* @Test
    fun largePrimeField1() = runBlocking {
        val statistic = TaskTimingStatistic(EqualTwoMatrixMultiplicationTiming<ModularUInt>())

        for (i in 1510u..1510u) {
            val primeField = PrimeUInt(primeOf(i).toUInt()).primeField
            val root = primeField.firstGenerator
            val dft = when (root) {
                is RootUIntPPP -> PrimeDftMatrixModularUInt(root.primeSubrootAt(root.order.factors.size.toUInt() - 1u))
                is RootUIntPP  -> PrimeDftMatrixModularUInt(root.primeSubroot())
                is RootUIntP   -> PrimeDftMatrixModularUInt(root)
                else           -> error("unknown type of root $root, class: ${root::class}")
            }
            val x = primeField.randomMatrix(dft.columns, 2u)
            statistic.go(TwoMatrix(dft, x))
            statistic.go(TwoMatrix(dft, x)) //repeat to prevent exception in statistic, but cost double time.
        }
        statistic.printAverageAndStandardDeviation()
    }*/

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

        val map = mutableMapOf<UInt, TaskTimingStatistic<TwoDftAndX, AbstractMatrix<ModularUInt>>>()
        val tasks = object : EqualTaskTiming<TwoDftAndX, AbstractMatrix<ModularUInt>> {
            override fun equals(a: AbstractMatrix<ModularUInt>, b: AbstractMatrix<ModularUInt>): Boolean = matrixEquals(a, b)

            override val tasks: List<Task<TwoDftAndX, AbstractMatrix<ModularUInt>>> = listOf(
                Task("ordinary") { (ordinary, fast, x) -> (ordinary * x).andPrint("ordi") },
                Task("    fast") { (ordinary, fast, x) -> (fast * x).andPrint("fast") }
            )
        }

        fun getStatistic(prime: UInt) = map.computeIfAbsent(prime) { TaskTimingStatistic(tasks) }

        suspend fun test(primeField: FieldModularUInt) {
//            println()
//            println("prime: ${primeField.prime}")
            val root = primeField.firstGenerator
            when (root) {
                is RootUIntPPP -> {
                    for (i in 0u until root.order.factors.size.toUInt()) {
                        val primeRoot = root.primeSubrootAt(i)
                        val dftOrdinary = PrimeDftMatrix(primeRoot)
                        val dftFast = PrimeDftMatrixModularUInt(primeRoot)
                        val statistic = getStatistic(primeRoot.order.prime)
                        repeat(2) {
                            val x = primeField.randomMatrix(dftFast.columns, 2u)
//                            println("o: ")
//                            println(matrixToString(dftOrdinary))
//                            println("f: ")
//                            println(matrixToString(dftFast))
//                            println("x: ")
//                            println(matrixToString(x))

                            statistic.go(TwoDftAndX(dftOrdinary, dftFast, x))
                        }
                    }
                }
                is RootUIntPP  -> {
                    val primeRoot = root.primeSubroot()
                    val dftOrdinary = PrimeDftMatrix(primeRoot)
                    val dftFast = PrimeDftMatrixModularUInt(primeRoot)
                    val statistic = getStatistic(primeRoot.order.prime)
                    repeat(2) {
                        val x = primeField.randomMatrix(dftFast.columns, 2u)
                        statistic.go(TwoDftAndX(dftOrdinary, dftFast, x))
                    }
                }
                is RootUIntP   -> {
                    val dftOrdinary = PrimeDftMatrix(root)
                    val dftFast = PrimeDftMatrixModularUInt(root)
                    val statistic = getStatistic(root.order.prime)
                    repeat(2) {
                        val x = primeField.randomMatrix(dftFast.columns, 2u)
                        statistic.go(TwoDftAndX(dftOrdinary, dftFast, x))
                    }
                }
                else           -> error("unknown type of root $root, class: ${root::class}")
            }
        }

        runBlocking {
            for (i in 1u..500u) {
                val primeField = PrimeUInt(primeOf(i).toUInt()).primeField
                repeat(3) {
                    test(primeField)
                }
            }

            for ((key, statistic) in map.entries.sortedBy { it.key }) {
                println("prime: $key")
                statistic.printAverageAndStandardDeviation()
                println()
            }
        }
    }

}