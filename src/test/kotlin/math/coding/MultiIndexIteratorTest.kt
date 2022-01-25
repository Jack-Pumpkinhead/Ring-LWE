package math.coding

import math.coding.iterator.MultiIndexIterator
import org.junit.jupiter.api.Test
import kotlin.random.Random
import kotlin.random.nextUInt

/**
 * Created by CowardlyLion at 2022/1/24 20:16
 */
internal class MultiIndexIteratorTest {

    @Test
    fun test() {
        for (length in 1..5) {
            repeat(3) {
                val radices = List(length) { Random.nextUInt(1u..7u) }
                println("radices: ${radices.joinToString(", ", "[", "]")}")
                val iterator = MultiIndexIterator(radices)
                for (code in iterator) {
                    println(code)
                }
                println()
            }
        }
    }

}