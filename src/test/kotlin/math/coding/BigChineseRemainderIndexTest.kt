package math.coding

import com.ionspin.kotlin.bignum.integer.toBigInteger
import math.abstract_structure.instance.RingBigInteger
import math.isPairwiseCoprimeBigInteger
import math.nonNegMin
import math.operation.product
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.random.Random
import kotlin.random.nextUInt
import kotlin.random.nextULong

/**
 * Created by CowardlyLion at 2022/1/15 21:24
 */
internal class BigChineseRemainderIndexTest {

    @Test
    fun encodeDecode() {
        for (length in 1..10) {
            repeat(length * 100 + length * length * 10) {
                val bounds = List(length) { Random.nextUInt(2u..100u).toBigInteger() }
                if (bounds.isPairwiseCoprimeBigInteger()) {
                    println("bound: ${bounds.joinToString(", ", "[", "]")}")
                    val index = BigChineseRemainderIndex(bounds, RingBigInteger.product(bounds))
                    repeat(1000) {
                        val i = List(length) { Random.nextUInt(index.bounds[it].uintValue()).toBigInteger() }
                        val c = index.encode(i)
                        println("${i.joinToString(", ", "[", "]")} = $c")
                        assertEquals(i, index.decode(c))
                    }
                }
            }
        }
    }

    @Test
    fun decodeEncode() {
        for (length in 1..10) {
            repeat(length * 100 + length * length * 10) {
                val bounds = List(length) { Random.nextUInt(2u..100u).toBigInteger() }
                if (bounds.isPairwiseCoprimeBigInteger()) {
                    println("bound: ${bounds.joinToString(", ", "[", "]")}")
                    val index = BigChineseRemainderIndex(bounds, RingBigInteger.product(bounds))
                    repeat(1000) {
                        val c = Random.nextULong(nonNegMin(index.indexBound, ULong.MAX_VALUE)).toBigInteger()
                        val i = index.decode(c)
                        println("$c = ${i.joinToString(", ", "[", "]")}")
                        assertEquals(c, index.encode(i))
                    }
                }
            }
        }
    }

}