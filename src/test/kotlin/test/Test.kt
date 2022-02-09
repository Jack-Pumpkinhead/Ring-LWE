package test

import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import org.junit.jupiter.api.Test

/**
 * Created by CowardlyLion at 2022/1/5 17:23
 */
internal class Test {

    /*@Test
    fun testBigInteger() {
        for (i in 0..1234) {
            val x = BigInteger(i)
            println("$i.numberOfDecimalDigits() = ${x.numberOfDecimalDigits()}")
        }
    }*/


    /*@Test
    fun testTPM() {
        val tpm = TpmFactory.platformTpm()
        val randomBytes = tpm.GetRandom(10)!!
        println(randomBytes.joinToString())
    }*/

    /*@Test
    fun launch() {
        runBlocking {
            //launch don't freeze captured variable
            *//*var i = 0u
            launch {
                delay(5L)
                println(i)
            }
            delay(5L)
            i++*//*

            *//*for (i in 0u..10u) {
                launch {
                    delay(10 - i.toLong())  //10,9,8,7,6,5,4,3,2,0,1
                    println(i)
                }
            }*//*

            //[i] is val, so would not change
            for (i in 0u..10u) {
                launch {
                    delay((10 - i.toLong()) * 1000)  //10,9,8,7,6,5,4,3,2,0,1
                    println(i)
                }
            }

        }
    }*/

    @Test
    fun bigDecimal() {
        val a = 123L.toBigDecimal(0L, null)
        "aaa".toBigDecimal()
        println(a)
    }
}