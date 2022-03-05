package cryptography.lattice.ring_lwe.matrix.canonical_embedding

import cryptography.lattice.ring_lwe.matrix.canonical_embedding.modular_uint.CeMatrixBuilderModularUInt
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import util.test.PrimeFieldCeInversePPITestBase

/**
 * Created by CowardlyLion at 2022/2/27 21:22
 */
internal class CeInverseMatrixPTest {

    //d* become faster?
    // *  : average 0.24924ms, deviation 1.05813ms
    // *p : average 0.34569ms, deviation 0.98551ms
    // * t: average 0.23356ms, deviation 1.05838ms
    // *pt: average 0.32803ms, deviation 0.91335ms
    //d*  : average 51.07118ms, deviation 507.29741ms
    //samples: 533, total time: 27.837360400s
    //range: 1..200
    @Test
    fun multiplication() = runBlocking {
        PrimeFieldCeInversePPITestBase(CeMatrixBuilderModularUInt).test(1u..200u)
    }

}