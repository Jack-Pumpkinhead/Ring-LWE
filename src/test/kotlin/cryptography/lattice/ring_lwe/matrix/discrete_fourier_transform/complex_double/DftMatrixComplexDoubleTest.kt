package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.complex_double

import kotlinx.coroutines.runBlocking
import math.complex_number.FieldComplexNumberDouble
import math.integer.uint.factored.primeFactorization
import math.integer.ulong.primeOf
import math.operation.maxAbsoluteDistance
import math.operation.multiply
import math.random.randomMatrix
import org.junit.jupiter.api.Test
import util.test.ComplexFieldDftTestBase

/**
 * Created by CowardlyLion at 2022/2/4 16:58
 */
internal class DftMatrixComplexDoubleTest {

    //more and more accurate now
    //1.5050093560232344E-11
    @Test
    fun approximatelyEquals() {
        runBlocking {
            val prime = primeOf(1000u).toUInt()
            val dft = DftMatrixCreatorComplexDouble.compute(1u, (prime - 1u).primeFactorization())
            val x = FieldComplexNumberDouble.randomMatrix(dft.columns, 2u, 10.0)
            val m1 = dft * x
            val m2 = multiply(dft, x)
            println(maxAbsoluteDistance(m1, m2))
        }
    }

    //a bit slower than DftMatrix on Z/(p), but significantly faster now
    // *  : average 4.85873ms, deviation 8.86641ms
    // *p : average 4.83896ms, deviation 7.39627ms
    // * t: average 4.57494ms, deviation 7.12251ms
    // *pt: average 5.99662ms, deviation 7.02938ms
    //d*  : average 37.92780ms, deviation 35.72035ms
    //samples: 200, total time: 11.639411500s
    //average distance: 6.708976764051872E-11, max distance: 2.972475808319795E-10
    //range: 1..200
    @Test
    fun multiplication() = runBlocking {
        ComplexFieldDftTestBase(DftMatrixBuilderComplexDouble).test(1u..200u)
    }

    //significantly faster now, only a bit slower than DftMatrix on Z/(p)
    // *  : average 76.71927ms, deviation 83.05970ms
    // *p : average 61.85092ms, deviation 52.62190ms
    // * t: average 56.69418ms, deviation 52.11741ms
    // *pt: average 66.24658ms, deviation 51.17152ms
    //d*  : average 733.80793ms, deviation 17.73720ms
    //samples: 11, total time: 10.948507700s
    //average distance: 1.2671090963379495E-9, max distance: 1.6404339893537053E-9
    //range: 410..420
    @Test
    fun largeMultiplication() = runBlocking {
        ComplexFieldDftTestBase(DftMatrixBuilderComplexDouble).test(410u..420u)
    }

}