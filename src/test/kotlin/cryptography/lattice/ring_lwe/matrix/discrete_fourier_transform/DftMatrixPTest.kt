package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform

import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.default_impl.DftMatrixBuilderImpl
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import util.test.PrimeFieldDftPTestBase

/**
 * Created by CowardlyLion at 2022/1/19 23:13
 */
internal class DftMatrixPTest {

    //significantly faster and time-stable (because it simply uses direct method of multiplication)
    // *  : average 4.21447ms, deviation 14.78655ms
    // *p : average 4.63915ms, deviation 15.29717ms
    // * t: average 4.25909ms, deviation 14.90202ms
    // *pt: average 4.86553ms, deviation 16.21138ms
    //d*  : average 4.19369ms, deviation 14.77283ms
    //samples: 500, total time: 11.085964200s
    //range: 1..500
    @Test
    fun primeField() = runBlocking {
        PrimeFieldDftPTestBase(DftMatrixBuilderImpl()).test(1u..500u)
    }

    // *  : average 26.42778ms, deviation 46.03061ms
    // *p : average 27.63872ms, deviation 47.92050ms
    // * t: average 22.93592ms, deviation 37.55120ms
    // *pt: average 25.51128ms, deviation 41.92718ms
    //d*  : average 21.47180ms, deviation 32.92979ms
    //samples: 10, total time: 1.239855s
    //range: 1500..1509
    @Test
    fun largePrimeField() = runBlocking {
        PrimeFieldDftPTestBase(DftMatrixBuilderImpl()).test(1500u..1509u)
    }

    //i: 1510,  prime: 12647,  p-1: (12646 = [2, 6323])
    // *  : average 1670.67790ms, deviation 38.72866ms
    // *p : average 1712.75535ms, deviation 45.83459ms
    // * t: average 1814.48680ms, deviation 20.87874ms
    // *pt: average 1760.90085ms, deviation 5.97413ms
    //d*  : average 1647.89745ms, deviation 2.31061ms
    //samples: 2, total time: 17.213436700s
    //range: 1510..1510
    @Test
    fun largePrimeField1() = runBlocking {
        PrimeFieldDftPTestBase(DftMatrixBuilderImpl()).test(1510u..1510u, 2u)
    }

}