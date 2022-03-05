package cryptography.lattice.ring_lwe.matrix.canonical_embedding_conjugate_transpose.complex_double

import cryptography.lattice.ring_lwe.ring.RootPPI
import cryptography.lattice.ring_lwe.ring.RootPPP
import cryptography.lattice.ring_lwe.ring.root.RootCalculatorUnsafeComplexNumber
import cryptography.lattice.ring_lwe.ring.subroot.SubrootCalculatorUnsafeComplexDouble
import kotlinx.coroutines.runBlocking
import math.complex_number.FieldComplexNumberDouble
import math.integer.uint.factored.primeFactorization
import math.integer.ulong.primeOf
import math.operation.maxAbsoluteDistance
import math.operation.multiply
import math.random.randomMatrix
import org.junit.jupiter.api.Test
import util.test.ComplexFieldCeCTPPITestBase

/**
 * Created by CowardlyLion at 2022/2/24 19:33
 */
internal class CeCTMatrixPPIBuilderComplexDoubleTest {

    //(7932 = [2^2, 3, 661])
    //0.0
    //0.0
    //1.5441334513843364E-11
    @Test
    fun approximatelyEquals() {
        runBlocking {
            val prime = primeOf(1001u).toUInt()
            val primeDec = (prime - 1u).primeFactorization()
            println(primeDec)
            val root = RootCalculatorUnsafeComplexNumber.compute(1u, primeDec)
            when (root) {
                is RootPPP -> {
                    for (subroot in SubrootCalculatorUnsafeComplexDouble.allMaximalPrimePowerSubroot(root)) {
                        val ce_ct = CeCTMatrixBuilderComplexDouble.compute(subroot)
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
                is RootPPI -> {
                    val ce_ct = CeCTMatrixBuilderComplexDouble.compute(root)
                    val x = FieldComplexNumberDouble.randomMatrix(ce_ct.columns, 2u, 100.0)
                    val m1 = ce_ct * x
                    val m2 = multiply(ce_ct, x)
                    println(maxAbsoluteDistance(m1, m2))
                }
                else       -> error("unknown type of $root, class: ${root::class}")
            }

        }
    }


    // *  : average 0.53771ms, deviation 2.78420ms
    // *p : average 0.62993ms, deviation 2.85127ms
    // * t: average 0.45930ms, deviation 2.29025ms
    // *pt: average 0.60641ms, deviation 2.46747ms
    //d*  : average 0.53669ms, deviation 2.60002ms
    //samples: 533, total time: 1.476431500s
    //average distance: 6.090469483369366E-13, max distance: 6.046600952175017E-11
    //range: 1..200
    @Test
    fun multiplication() = runBlocking {
        ComplexFieldCeCTPPITestBase(CeCTMatrixBuilderComplexDouble).test(1u..200u)
    }

    // *  : average 10.51557ms, deviation 29.70347ms
    // *p : average 10.80253ms, deviation 28.44076ms
    // * t: average 7.05991ms, deviation 19.03615ms
    // *pt: average 8.46498ms, deviation 21.95210ms
    //d*  : average 15.73803ms, deviation 48.90645ms
    //samples: 32, total time: 1.682592100s
    //average distance: 2.9941705358644114E-11, max distance: 3.81970450643179E-10
    //range: 410..420
    @Test
    fun multiplicationLarge() = runBlocking {
        ComplexFieldCeCTPPITestBase(CeCTMatrixBuilderComplexDouble).test(410u..420u)
    }

}