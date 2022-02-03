package math.random

import org.junit.jupiter.api.Test
import util.stdlib.shl

/**
 * Created by CowardlyLion at 2022/2/3 13:09
 */
internal class TPMRandomTest {

    @Test
    fun nextBits() {
        for (i in 0u until 32u) {
            println("i: $i")
            val bound = 1u shl i
            repeat(100) {
//                println(it)
                val r = TPMRandom.nextBits(i)
                if (r >= bound) {
                    println("r: $r, bound: $bound")
                }
                require(r < bound)
            }
        }
    }

}