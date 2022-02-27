package cryptography.lattice.ring_lwe.matrix.canonical_embedding.conjugate_transpose

import cryptography.lattice.ring_lwe.ring.RootUIntPPI
import cryptography.lattice.ring_lwe.ring.RootUIntPPP
import kotlinx.coroutines.runBlocking
import math.abstract_structure.instance.FieldComplexNumberDouble
import math.integer.uint.factored.primeFactorization
import math.integer.ulong.primeOf
import math.operation.maxAbsoluteDistance
import math.operation.multiply
import math.random.randomMatrix
import math.statistic.TaskNearMatrixComplexDoubleStatistic
import math.timing.TwoMatrix
import math.timing.TwoMatrixMultiplicationTiming
import org.junit.jupiter.api.Test

/**
 * Created by CowardlyLion at 2022/2/24 19:33
 */
internal class CeCTMatrixPPIBuilderComplexDoubleTest{

    //(7932 = [2^2, 3, 661])
    //3.0597022343079476E-11
    //8.716123795936464E-12
    //3.548244702744927E-7
    @Test
    fun approximatelyEquals() {
        runBlocking {
            val prime = primeOf(1001u).toUInt()
            val primeDec = (prime - 1u).primeFactorization()
            println(primeDec)
            val root = FieldComplexNumberDouble.root(primeDec)
            when (root) {
                is RootUIntPPP -> {
                    for (subroot in root.allMaximalPrimePowerSubroot()) {
                        val ce_ct = CeCTMatrixPPIBuilderComplexDouble.build(subroot)
                        val x = FieldComplexNumberDouble.randomMatrix(ce_ct.columns, 2u, 100.0)
                        val m1 = ce_ct * x
                        val m2 = multiply(ce_ct, x)
//                        println("ce_ct: ${matrixToString(ce_ct)}")
//                        println("x: ${matrixToString(x)}")
//                        println("m1: ${matrixToString(m1)}")
//                        println("m2: ${matrixToString(m2)}")
                        println(maxAbsoluteDistance(m1, m2))
                    }
                }
                is RootUIntPPI -> {
                    val ce_ct = CeCTMatrixPPIBuilderComplexDouble.build(root)
                    val x = FieldComplexNumberDouble.randomMatrix(ce_ct.columns, 2u, 100.0)
                    val m1 = ce_ct * x
                    val m2 = multiply(ce_ct, x)
                    println(maxAbsoluteDistance(m1, m2))
                }
                else           -> error("unknown type of $root, class: ${root::class}")
            }

        }
    }

    fun testBase(range: UIntRange) {
        runBlocking {
            val statistic = TaskNearMatrixComplexDoubleStatistic(TwoMatrixMultiplicationTiming())
            for (i in range) {
                val prime = primeOf(i).toUInt()
                val root = FieldComplexNumberDouble.root((prime - 1u).primeFactorization())
                when (root) {
                    is RootUIntPPP -> {
                        for (subroot in root.allMaximalPrimePowerSubroot()) {
                            val ce_ct = CeCTMatrixPPIBuilderComplexDouble.build(subroot)
                            val x = FieldComplexNumberDouble.randomMatrix(ce_ct.columns, 2u, 100.0)
                            statistic.go(TwoMatrix(ce_ct, x))
                        }
                    }
                    is RootUIntPPI -> {
                        val ce_ct = CeCTMatrixPPIBuilderComplexDouble.build(root)
                        val x = FieldComplexNumberDouble.randomMatrix(ce_ct.columns, 2u, 100.0)
                        statistic.go(TwoMatrix(ce_ct, x))
                    }
                    else           -> error("unknown type of $root, class: ${root::class}")
                }
            }
            statistic.printAverageAndStandardDeviation()
            statistic.printAverageAndMaxDistance()
            println("range: $range")
        }
    }

    // *  : average 0.52397ms, deviation 2.96453ms
    // *p : average 0.59750ms, deviation 2.63850ms
    // * t: average 0.41802ms, deviation 1.92112ms
    // *pt: average 0.59172ms, deviation 2.39860ms
    //d*  : average 0.51813ms, deviation 2.44875ms
    //samples: 533, total time: 1.412099615s
    //average distance: 1.2273413103560842E-9, max distance: 3.345378575961774E-7
    //range: 1..200
    @Test
    fun multiplication() {
        testBase(1u..200u)
    }

    // *  : average 9.01969ms, deviation 25.06768ms
    // *p : average 9.56040ms, deviation 24.69102ms
    // * t: average 6.12773ms, deviation 17.11796ms
    // *pt: average 7.85334ms, deviation 20.62398ms
    //d*  : average 15.87171ms, deviation 49.35905ms
    //samples: 32, total time: 1.549851897s
    //average distance: 1.2061960329129685E-7, max distance: 1.5941826608342162E-6
    //range: 410..420
    @Test
    fun multiplicationLarge() {
        testBase(410u..420u)
    }

}