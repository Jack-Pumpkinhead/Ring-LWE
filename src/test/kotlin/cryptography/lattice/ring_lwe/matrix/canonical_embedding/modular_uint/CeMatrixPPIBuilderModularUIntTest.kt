package cryptography.lattice.ring_lwe.matrix.canonical_embedding.modular_uint

import cryptography.lattice.ring_lwe.ring.RootUIntPPI
import cryptography.lattice.ring_lwe.ring.RootUIntPPP
import kotlinx.coroutines.runBlocking
import math.integer.uint.modular.FieldModularUInt
import math.integer.uint.modular.ModularUInt
import math.integer.ulong.primeOf
import math.martix.AbstractMatrix
import math.operation.matrixEquals
import math.operation.toStringM
import math.random.randomMatrix
import math.statistic.TaskTimingStatistic
import math.timing.*
import org.junit.jupiter.api.Test

/**
 * Created by CowardlyLion at 2022/2/24 17:26
 */
internal class CeMatrixPPIBuilderModularUIntTest {

    fun base(range: UIntRange) {
        runBlocking {
            val statistic = TaskTimingStatistic(EqualTwoMatrixMultiplicationTiming<ModularUInt>())
            for (i in range) {
                val primeField = FieldModularUInt(primeOf(i).toUInt())

                val root = primeField.firstGenerator
                when (root) {
                    is RootUIntPPP -> {
                        for (subroot in root.allMaximalPrimePowerSubroot()) {
                            val ce = CeMatrixPPIBuilderModularUInt.build(subroot)
                            val x = primeField.randomMatrix(ce.columns, 2u)
                            statistic.go(TwoMatrix(ce, x))
                        }
                    }
                    is RootUIntPPI -> {
                        val ce = CeMatrixPPIBuilderModularUInt.build(root)
                        val x = primeField.randomMatrix(ce.columns, 2u)
                        statistic.go(TwoMatrix(ce, x))
                    }
                    else           -> error("unknown type of $root, class: ${root::class}")
                }
            }
            statistic.printAverageAndStandardDeviation()
            println("range: $range")
        }
    }

    //slower than direct multiplication
    // *  : average 0.33774ms, deviation 1.44389ms
    // *p : average 0.46930ms, deviation 1.55755ms
    // * t: average 0.28859ms, deviation 1.27683ms
    // *pt: average 0.45600ms, deviation 1.50964ms
    //d*  : average 0.28277ms, deviation 1.31135ms
    //samples: 533, total time: 977.737400ms
    //range: 1..200
    @Test
    fun multiplication() {
        base(1u..200u)
    }

    // *  : average 7.51156ms, deviation 29.68362ms
    // *p : average 6.59645ms, deviation 23.94152ms
    // * t: average 5.47433ms, deviation 20.05112ms
    // *pt: average 6.50317ms, deviation 23.38281ms
    //d*  : average 9.22518ms, deviation 37.70856ms
    //samples: 301, total time: 10.628523200s
    //range: 500..600
    @Test
    fun multiplicationLarge() {
        base(500u..600u)
    }

    // *  : average 22.91784ms, deviation 71.02473ms
    // *p : average 15.49169ms, deviation 43.80464ms
    // * t: average 11.04374ms, deviation 32.88266ms
    // *pt: average 15.17582ms, deviation 43.55964ms
    //d*  : average 27.28074ms, deviation 96.23372ms
    //samples: 36, total time: 3.308754300s
    //range: 700..712
    @Test
    fun multiplicationLarge1() {
        base(700u..712u)
    }



    @Test
    fun inverseCorrectness0() {
        runBlocking {
            val statistic = TaskTimingStatistic(
                EqualTaskTimingImpl<ThreeMatrix<ModularUInt>, AbstractMatrix<ModularUInt>>(
                    Task("A A^-1") { (a, a_inv, x) -> a * (a_inv * x) },
                    Task("A^-1 A") { (a, a_inv, x) -> a_inv * (a * x) },
                    Task("    id") { (a, a_inv, x) -> x },
                )
            )
            val range = 1u..20u
            for (i in range) {
                val primeField = FieldModularUInt(primeOf(i).toUInt())
                val root = primeField.firstGenerator
                println("root: ${root.root}, order: ${root.order}")
                when (root) {
                    is RootUIntPPP -> {
                        for (subroot in root.allMaximalPrimePowerSubroot()) {
                            val ce = CeMatrixPPIBuilderModularUInt.build(subroot)
                            val x = primeField.randomMatrix(ce.columns, 2u)
                            println("ce: ${ce.toStringM()}")
                            println("inv: ${ce.inverse.toStringM()}")
                            println("ce inv: ${(ce * ce.inverse).toStringM()}")
//                            println("x: ${x.toStringM()}")
//                            println("ce * inv * x: ${(ce * (ce.inverse * x)).toStringM()}")
//                            statistic.go(ThreeMatrix(ce, ce.inverse, x))
                        }
                    }
                    is RootUIntPPI -> {
                        val ce = CeMatrixPPIBuilderModularUInt.build(root)
                        val x = primeField.randomMatrix(ce.columns, 2u)
                        println("ce: ${ce.toStringM()}")
                        println("inv: ${ce.inverse.toStringM()}")
                        println("ce inv: ${(ce * ce.inverse).toStringM()}")
//                        println("x: ${x.toStringM()}")
//                        println("ce * inv * x: ${(ce * (ce.inverse * x)).toStringM()}")
//                        statistic.go(ThreeMatrix(ce, ce.inverse, x))
                    }
                    else           -> error("unknown type of $root, class: ${root::class}")
                }
                println()
            }
//            statistic.printAverageAndStandardDeviation()
//            println("range: $range")
        }
    }

    //A A^-1: average 0.56325ms, deviation 2.43442ms
    //A^-1 A: average 0.52415ms, deviation 2.39265ms
    //    id: average 0.00127ms, deviation 0.00344ms
    //samples: 533, total time: 580.260300ms
    //range: 1..200
    @Test
    fun inverseCorrectness() {
        runBlocking {
            val statistic = TaskTimingStatistic(
                object : EqualTaskTiming<ThreeMatrix<ModularUInt>, AbstractMatrix<ModularUInt>> {
                    override val tasks: List<Task<ThreeMatrix<ModularUInt>, AbstractMatrix<ModularUInt>>> = listOf(
                        Task("A A^-1") { (a, a_inv, x) -> a * (a_inv * x) },
                        Task("A^-1 A") { (a, a_inv, x) -> a_inv * (a * x) },
                        Task("    id") { (a, a_inv, x) -> x },
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
                    is RootUIntPPP -> {
                        for (subroot in root.allMaximalPrimePowerSubroot()) {
                            val ce = CeMatrixPPIBuilderModularUInt.build(subroot)
                            val x = primeField.randomMatrix(ce.columns, 2u)
                            statistic.go(ThreeMatrix(ce, ce.inverse, x))
                        }
                    }
                    is RootUIntPPI -> {
                        val ce = CeMatrixPPIBuilderModularUInt.build(root)
                        val x = primeField.randomMatrix(ce.columns, 2u)
                        statistic.go(ThreeMatrix(ce, ce.inverse, x))
                    }
                    else           -> error("unknown type of $root, class: ${root::class}")
                }
            }
            statistic.printAverageAndStandardDeviation()
            println("range: $range")
        }
    }


}