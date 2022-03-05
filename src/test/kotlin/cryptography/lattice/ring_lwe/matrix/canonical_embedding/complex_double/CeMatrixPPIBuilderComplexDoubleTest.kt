package cryptography.lattice.ring_lwe.matrix.canonical_embedding.complex_double

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
import util.errorUnknownObject
import util.test.ComplexFieldCePPITestBase

/**
 * Created by CowardlyLion at 2022/2/24 17:41
 */
internal class CeMatrixPPIBuilderComplexDoubleTest {

    //more exact
    //prime: 7927
    //0.0
    //0.0
    //2.6461450990566217E-11
    @Test
    fun approximatelyEquals() = runBlocking {
        val builder = CeMatrixBuilderComplexDouble
        val prime = primeOf(1000u).toUInt()
        println("prime: $prime")
        val root = RootCalculatorUnsafeComplexNumber.compute(1u, (prime - 1u).primeFactorization())
        when (root) {
            is RootPPP -> {
                for (subroot in SubrootCalculatorUnsafeComplexDouble.allMaximalPrimePowerSubroot(root)) {
                    val ce = builder.compute(subroot)
                    val x = FieldComplexNumberDouble.randomMatrix(ce.columns, 2u, 100.0)
                    val m1 = ce * x
                    val m2 = multiply(ce, x)
                    println(maxAbsoluteDistance(m1, m2))
                }
            }
            is RootPPI -> {
                val ce = builder.compute(root)
                val x = FieldComplexNumberDouble.randomMatrix(ce.columns, 2u, 100.0)
                val m1 = ce * x
                val m2 = multiply(ce, x)
                println(maxAbsoluteDistance(m1, m2))
            }
            else       -> errorUnknownObject(root)
        }
    }

    //slower than direct multiplication, more exact
    // *  : average 0.50967ms, deviation 2.29698ms
    // *p : average 0.62364ms, deviation 2.29709ms
    // * t: average 0.49933ms, deviation 2.32459ms
    // *pt: average 0.60339ms, deviation 2.32595ms
    //d*  : average 0.44284ms, deviation 2.13500ms
    //samples: 533, total time: 1.427838s
    //average distance: 7.015899080753834E-14, max distance: 1.1395935949750917E-11
    //range: 1..200
    @Test
    fun multiplication() = runBlocking {
        ComplexFieldCePPITestBase(CeMatrixBuilderComplexDouble).test(1u..200u)
    }

    //more exact, * slower a bit
    //faster than direct multiplication
    // *  : average 12.72141ms, deviation 37.47447ms
    // *p : average 10.89145ms, deviation 28.82445ms
    // * t: average 6.66397ms, deviation 16.85602ms
    // *pt: average 8.51042ms, deviation 21.51279ms
    //d*  : average 14.27965ms, deviation 45.23243ms
    //samples: 32, total time: 1.698140700s
    //average distance: 2.856609970071737E-11, max distance: 3.974564688905277E-10
    //range: 410..420
    @Test
    fun multiplicationLarge() = runBlocking {
        ComplexFieldCePPITestBase(CeMatrixBuilderComplexDouble).test(410u..420u)
    }


}