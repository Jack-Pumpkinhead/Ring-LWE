package math.coding

import com.ionspin.kotlin.bignum.integer.toBigInteger
import math.abstract_structure.instance.RingBigInteger
import math.nonNegMin
import math.operation.product
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

import kotlin.random.Random
import kotlin.random.nextUInt
import kotlin.random.nextULong

/**
 * Created by CowardlyLion at 2022/1/15 14:22
 */
internal class BigLadderIndexTest {

    @Test
    fun encodeDecode() {
        for (length in 1..10) {
            repeat(length * 20) {
                val bounds = List(length) { Random.nextUInt(1u..100u).toBigInteger() }
//                println("bound: ${bounds.joinToString(", ", "[", "]")}")
                val index = BigLadderIndex(bounds, RingBigInteger.product(bounds))
                repeat(1000) {
                    val i = List(length) { Random.nextUInt(index.bounds[it].uintValue()).toBigInteger() }
                    val c = index.encode(i)
//                    println("${i.joinToString(", ", "[", "]")} = $c")
                    assertEquals(i, index.decode(c))
                }
            }
        }
    }

    @Test
    fun decodeEncode() {
        for (length in 1..10) {
            repeat(length * 20) {
                val bounds = List(length) { Random.nextUInt(1u..100u).toBigInteger() }
//                println("bound: ${bounds.joinToString(", ", "[", "]")}")
                val index = BigLadderIndex(bounds, RingBigInteger.product(bounds))
                repeat(1000) {
                    val c = Random.nextULong(nonNegMin(index.indexBound, ULong.MAX_VALUE)).toBigInteger()
                    val i = index.decode(c)
//                    println("$c = ${i.joinToString(", ", "[", "]")}")
                    assertEquals(c, index.encode(i))
                }
            }
        }
    }
}