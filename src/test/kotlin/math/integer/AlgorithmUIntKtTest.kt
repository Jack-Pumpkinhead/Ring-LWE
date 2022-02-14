package math.integer

import math.integer.uint.ceilLog2
import math.integer.uint.floorLog2Int
import math.integer.uint.isTwoPower
import math.integer.uint.nextTwoPositivePower
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import util.stdlib.shl

/**
 * Created by CowardlyLion at 2022/2/2 21:35
 */
internal class AlgorithmUIntKtTest {
    @Test
    fun floorLog() {
        for (i in 1u..123456u) {
//            println(i)
            val log = i.floorLog2Int()
            val p = 1uL shl log
            assertTrue(p <= i)
            assertTrue(i < (p shl 1))
        }
    }

    @Test
    fun ceilLog() {
        assertEquals(1u.ceilLog2(), 0u)
        for (i in 2u..123456u) {
//            println(i)
            val log = i.ceilLog2()
            val p = 1uL shl  log
            assertTrue(p shr 1 < i)
            assertTrue(i <= p)
        }
    }

    @Test
    fun nextTwoPowerTest() {
        for (i in 2u..123456u) {
//            println(i)
            val twoPower = nextTwoPositivePower(i)
            assertTrue(twoPower.value >= i)
            assertTrue(twoPower.value / 2u < i)
        }
    }

    @Test
    fun isTwoPowerTest() {
        for (i in 0u..123456u) {
            if (isTwoPower(i)) {
                println(i)
            }
        }
    }


}