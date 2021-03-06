package math.coding.iterator

import math.integer.uint.RingUInt
import math.operation.product
import org.junit.jupiter.api.Test
import kotlin.random.Random
import kotlin.random.nextUInt

/**
 * Created by CowardlyLion at 2022/1/24 18:17
 */
internal class MultiIndexIteratorGrayTest {


    @Test
    fun test() {
        for (length in 1..5) {
            repeat(3) {
                val bounds = List(length) { Random.nextUInt(1u..7u) }
                println("radices: ${bounds.joinToString(", ", "[", "]")}")
                val iterator = MultiIndexIteratorGrayImpl(bounds, RingUInt.product(bounds))
                for (code in iterator) {
                    println("code: $code, index: ${iterator.index}")
                }
                println()
            }
        }
    }

}