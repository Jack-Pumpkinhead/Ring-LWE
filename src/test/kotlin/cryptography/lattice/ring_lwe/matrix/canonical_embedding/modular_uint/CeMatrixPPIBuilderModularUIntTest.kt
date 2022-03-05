package cryptography.lattice.ring_lwe.matrix.canonical_embedding.modular_uint

import cryptography.lattice.ring_lwe.ring.RootPPI
import cryptography.lattice.ring_lwe.ring.RootPPP
import cryptography.lattice.ring_lwe.ring.subroot.SubrootCalculatorUnsafeModularUInt
import kotlinx.coroutines.runBlocking
import math.integer.uint.modular.FieldModularUInt
import math.integer.uint.modular.ModularUInt
import math.integer.ulong.primeOf
import math.martix.AbstractMatrix
import math.operation.matrixEquals
import math.operation.toStringM
import math.random.randomMatrix
import math.statistic.TaskTimingStatistic
import math.timing.EqualTaskTiming
import math.timing.EqualTaskTimingImpl
import math.timing.Task
import math.timing.ThreeMatrix
import org.junit.jupiter.api.Test
import util.test.PrimeFieldCePPITestBase

/**
 * Created by CowardlyLion at 2022/2/24 17:26
 */
internal class CeMatrixPPIBuilderModularUIntTest {

    //slower than direct multiplication
    // *  : average 0.31697ms, deviation 1.33863ms
    // *p : average 0.43330ms, deviation 1.43526ms
    // * t: average 0.31244ms, deviation 1.33434ms
    // *pt: average 0.42894ms, deviation 1.43195ms
    //d*  : average 0.25732ms, deviation 1.20624ms
    //samples: 533, total time: 932.204700ms
    //range: 1..200
    @Test
    fun multiplication() = runBlocking {
        PrimeFieldCePPITestBase(CeMatrixBuilderModularUInt).test(1u..200u)
    }

    // *  : average 7.61776ms, deviation 31.20911ms
    // *p : average 6.88151ms, deviation 25.08194ms
    // * t: average 5.75936ms, deviation 21.26552ms
    // *pt: average 6.59676ms, deviation 23.87961ms
    //d*  : average 8.95285ms, deviation 36.57185ms
    //samples: 301, total time: 10.778277s
    //range: 500..600
    @Test
    fun multiplicationLarge() = runBlocking {
        PrimeFieldCePPITestBase(CeMatrixBuilderModularUInt).test(500u..600u)
    }

    // *  : average 22.09022ms, deviation 67.03437ms
    // *p : average 16.66014ms, deviation 46.75680ms
    // * t: average 12.58605ms, deviation 37.25472ms
    // *pt: average 14.52900ms, deviation 41.60451ms
    //d*  : average 23.44931ms, deviation 81.60774ms
    //samples: 36, total time: 3.215329500s
    //range: 700..712
    @Test
    fun multiplicationLarge1() = runBlocking {
        PrimeFieldCePPITestBase(CeMatrixBuilderModularUInt).test(700u..712u)
    }


    @Test
    fun inverseCorrectness0() = runBlocking {
        val builder = CeMatrixBuilderModularUInt
        val statistic = TaskTimingStatistic(
            EqualTaskTimingImpl<ThreeMatrix<ModularUInt>, AbstractMatrix<ModularUInt>>(
                Task("A A^-1") { (a, a_inv, x) -> a * (a_inv * x) },
                Task("A^-1 A") { (a, a_inv, x) -> a_inv * (a * x) },
                Task("    id") { (_, _, x) -> x },
            )
        )
        val range = 1u..20u
        for (i in range) {
            val primeField = FieldModularUInt(primeOf(i).toUInt())
            val root = primeField.firstGenerator
            println("root: ${root.root}")
            when (root) {
                is RootPPP -> {
                    for (subroot in SubrootCalculatorUnsafeModularUInt.allMaximalPrimePowerSubroot(root)) {
                        val ce = builder.compute(subroot)
                        val x = primeField.randomMatrix(ce.columns, 2u)
                        println("ce: ${ce.toStringM()}")
                        println("inv: ${ce.inverse.toStringM()}")
                        println("ce inv: ${(ce * ce.inverse).toStringM()}")
//                            println("x: ${x.toStringM()}")
//                            println("ce * inv * x: ${(ce * (ce.inverse * x)).toStringM()}")
//                            statistic.go(ThreeMatrix(ce, ce.inverse, x))
                    }
                }
                is RootPPI -> {
                    val ce = builder.compute(root)
                    val x = primeField.randomMatrix(ce.columns, 2u)
                    println("ce: ${ce.toStringM()}")
                    println("inv: ${ce.inverse.toStringM()}")
                    println("ce inv: ${(ce * ce.inverse).toStringM()}")
//                        println("x: ${x.toStringM()}")
//                        println("ce * inv * x: ${(ce * (ce.inverse * x)).toStringM()}")
//                        statistic.go(ThreeMatrix(ce, ce.inverse, x))
                }
                else       -> error("unknown type of $root, class: ${root::class}")
            }
            println()
        }
//            statistic.printAverageAndStandardDeviation()
//            println("range: $range")

    }

    //A A^-1: average 0.61124ms, deviation 2.57694ms
    //A^-1 A: average 0.58282ms, deviation 2.65711ms
    //    id: average 0.00133ms, deviation 0.00221ms
    //samples: 533, total time: 637.143400ms
    //range: 1..200
    @Test
    fun inverseCorrectness() = runBlocking {
        val builder = CeMatrixBuilderModularUInt
        val statistic = TaskTimingStatistic(
            object : EqualTaskTiming<ThreeMatrix<ModularUInt>, AbstractMatrix<ModularUInt>> {
                override val tasks: List<Task<ThreeMatrix<ModularUInt>, AbstractMatrix<ModularUInt>>> = listOf(
                    Task("A A^-1") { (a, a_inv, x) -> a * (a_inv * x) },
                    Task("A^-1 A") { (a, a_inv, x) -> a_inv * (a * x) },
                    Task("    id") { (_, _, x) -> x },
                )

                override fun equals(a: AbstractMatrix<ModularUInt>, b: AbstractMatrix<ModularUInt>): Boolean = matrixEquals(a, b)
            }
        )
        val range = 1u..200u
        for (i in range) {
            val primeField = FieldModularUInt(primeOf(i).toUInt())

            val root = primeField.firstGenerator
//                println("root: ${root.root}, order: ${root.order}")
            when (root) {
                is RootPPP -> {
                    for (subroot in SubrootCalculatorUnsafeModularUInt.allMaximalPrimePowerSubroot(root)) {
                        val ce = builder.compute(subroot)
                        val x = primeField.randomMatrix(ce.columns, 2u)
                        statistic.go(ThreeMatrix(ce, ce.inverse, x))
                    }
                }
                is RootPPI -> {
                    val ce = builder.compute(root)
                    val x = primeField.randomMatrix(ce.columns, 2u)
                    statistic.go(ThreeMatrix(ce, ce.inverse, x))
                }
                else       -> error("unknown type of $root, class: ${root::class}")
            }
        }
        statistic.printAverageAndStandardDeviation()
        println("range: $range")

    }


}