package math.coding

import math.coding.iterator.GrayCodeIterator
import org.junit.jupiter.api.Test
import kotlin.random.Random
import kotlin.random.nextUInt

/**
 * Created by CowardlyLion at 2022/1/24 18:17
 */
internal class GrayCodeIteratorTest {


    @Test
    fun test() {
        for (length in 1..5) {
            repeat(3) {
                val radices = List(length) { Random.nextUInt(1u..7u) }
                println("radices: ${radices.joinToString(", ", "[", "]")}")
                val iterator = GrayCodeIterator(radices)
                for (code in iterator) {
                    println(code)
                }
                println()
            }
        }
    }

}