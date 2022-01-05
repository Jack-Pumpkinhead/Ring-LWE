package math.operations

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/**
 * Created by CowardlyLion at 2022/1/4 17:58
 */
internal class UIntOperationKtTest {

    @Test
    fun modPower() {
        for (modulus in 10u..100u) {
            for (x in 0u..100u) {
                for (power in 0u..100u) {
                    val modpowerM = modPowerM(x % modulus, power, modulus)
                    val modpowerS = modPowerSq(x % modulus, power, modulus)
//                    println("$x^$power = $modpowerM, $modpowerS \t mod $modulus")
                    assertEquals(modpowerM, modpowerS)
                }
            }
        }
    }

    @Test
    fun power() {
        for (x in 0u..100u) {
            for (power in 0u..100u) {
                val powerM = x.powerM(power)
                val powerS = x.powerSq(power)
//                println("$x^$power = $powerM, $powerS")
                assertEquals(powerM, powerS)
            }
        }
    }

}