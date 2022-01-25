package math.coding.iterator

import math.abstract_structure.instance.RingUInt
import math.operation.product
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
                val bounds = List(length) { Random.nextUInt(1u..7u) }
                println("radices: ${bounds.joinToString(", ", "[", "]")}")
                val iterator = MultiIndexIteratorImpl(bounds, RingUInt.product(bounds))
                for (code in iterator) {
                    println(code)
                }
                println()
            }
        }
    }

}