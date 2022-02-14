package math.integer

import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.toBigInteger
import math.integer.big_integer.ceilLog2
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/**
 * Created by CowardlyLion at 2022/2/11 17:04
 */
internal class AlgorithmBigIntegerKtTest {

    @Test
    fun ceilLog2() {
        assertEquals(BigInteger.ONE.ceilLog2(), 0uL)
        val bound = 123456u.toBigInteger()
        var i = BigInteger.TWO
        while (i <= bound) {
            val log = i.ceilLog2()
            val p = BigInteger.ONE.shl(log.toInt())
            assertTrue(p shr 1 < i)
            assertTrue(i <= p)
            i++
        }
    }
}