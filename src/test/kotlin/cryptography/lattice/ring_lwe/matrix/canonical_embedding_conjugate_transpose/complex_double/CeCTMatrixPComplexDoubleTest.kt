package cryptography.lattice.ring_lwe.matrix.canonical_embedding_conjugate_transpose.complex_double

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import util.test.ComplexFieldCeCTPTestBase

/**
 * Created by CowardlyLion at 2022/2/24 19:46
 */
internal class CeCTMatrixPComplexDoubleTest {

    // *  : average 0.51562ms, deviation 3.07748ms
    // *p : average 0.64067ms, deviation 3.29635ms
    // * t: average 0.43534ms, deviation 2.28392ms
    // *pt: average 0.58543ms, deviation 2.77345ms
    //d*  : average 0.49712ms, deviation 2.58001ms
    //samples: 533, total time: 1.425342s
    //average distance: 5.214183765508704E-13, max distance: 6.997791956263006E-11
    //range: 1..200
    @Test
    fun multiplication() = runBlocking {
        ComplexFieldCeCTPTestBase(CeCTMatrixBuilderComplexDouble).test(1u..200u)
    }

    // *  : average 9.64865ms, deviation 27.58806ms
    // *p : average 11.07293ms, deviation 30.13258ms
    // * t: average 6.97650ms, deviation 18.83715ms
    // *pt: average 8.89152ms, deviation 23.75114ms
    //d*  : average 15.82101ms, deviation 50.04152ms
    //samples: 32, total time: 1.677139600s
    //average distance: 3.234027939239936E-11, max distance: 4.692040767829743E-10
    //range: 410..420
    @Test
    fun multiplicationLarge() = runBlocking {
        ComplexFieldCeCTPTestBase(CeCTMatrixBuilderComplexDouble).test(410u..420u)
    }

}