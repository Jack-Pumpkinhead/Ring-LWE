package cryptography.lattice.ring_lwe.matrix.canonical_embedding.complex_double

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import util.test.ComplexFieldCePTestBase

/**
 * Created by CowardlyLion at 2022/3/5 14:08
 */
internal class CeMatrixPComplexDoubleTest {

    //unbelievable exact computation (because it only use direct multiplication)
    // *  : average 0.42367ms, deviation 2.04814ms
    // *p : average 0.56979ms, deviation 2.22175ms
    // * t: average 0.43379ms, deviation 2.05274ms
    // *pt: average 0.54744ms, deviation 2.23020ms
    //d*  : average 0.38873ms, deviation 1.94266ms
    //samples: 533, total time: 1.259706s
    //average distance: 0.0, max distance: 0.0
    //range: 1..200
    @Test
    fun multiplication() = runBlocking {
        ComplexFieldCePTestBase(CeMatrixBuilderComplexDouble).test(1u..200u)
    }

    // *  : average 15.14362ms, deviation 47.23608ms
    // *p : average 10.89208ms, deviation 29.39754ms
    // * t: average 6.56081ms, deviation 17.31205ms
    // *pt: average 8.48755ms, deviation 22.24187ms
    //d*  : average 16.58224ms, deviation 54.57596ms
    //samples: 32, total time: 1.845321700s
    //average distance: 2.773185448168743E-11, max distance: 3.580222167983674E-10
    //range: 410..420
    @Test
    fun multiplicationLarge() = runBlocking {
        ComplexFieldCePTestBase(CeMatrixBuilderComplexDouble).test(410u..420u)
    }

}