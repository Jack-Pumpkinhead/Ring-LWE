package math.operations

import com.ionspin.kotlin.bignum.integer.BigInteger
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

/**
 * Created by CowardlyLion at 2022/1/5 17:59
 */
internal class BigIntegerOperationKtTest {

    @Test
    fun getMaxIndexOfOne() {
        for (i in -100..1234) {
            val x = BigInteger(i)
            println("$i.maxIndexOfOne ${i.toString(2)}, ${x.maxIndexOfOne}")
        }
    }

    @Test
    fun modPowerM() {
        for (modulus in 10..100) {
            val modulusB = BigInteger(modulus)
            for (x in 0..100) {
                val xB = BigInteger(x)
                for (power in 0..100) {
                    val powerB = BigInteger(power)
                    val b = xB.toModularBigInteger(modulusB)
                    val modpowerM = modPowerM(xB.mod(modulusB), powerB, modulusB)
                    val modpowerS = b.pow(powerB).toBigInteger()
//                    println("$x^$power = $modpowerM, $modpowerS \t mod $modulus")
                    assertEquals(modpowerM, modpowerS)
                }
            }
        }
    }


}