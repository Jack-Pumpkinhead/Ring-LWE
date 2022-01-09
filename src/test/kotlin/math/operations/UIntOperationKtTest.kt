package math.operations

import math.abstract_structure.instance.ringUInt
import math.abstract_structure.instance.ringModularUInt
import math.modular_integer.UIntModular
import math.powerM
import math.powerS
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/**
 * Created by CowardlyLion at 2022/1/4 17:58
 */
internal class UIntOperationKtTest {

    @Test
    fun modPower() {
        for (modulus in 10u..100u) {
            val ringUIntModular = ringModularUInt(modulus)
            val monoid = ringUIntModular.toMultiplicativeMonoid()
            for (x in 0u until modulus) {
                for (power in 0u..100u) {
                    val modPowerM = modPowerM(x, power, modulus)
                    val modPowerS = modPowerSq(x, power, modulus)
                    val modPowerRM = monoid.powerM(UIntModular(modulus, x), power)
                    val modPowerRS = monoid.powerS(UIntModular(modulus, x), power)
//                    println("$x^$power = $modpowerM, $modpowerS \t mod $modulus")
                    assertEquals(modPowerM, modPowerS)
                    assertEquals(modPowerRM, modPowerRS)
                    assertEquals(modPowerM, modPowerRM.residue)
                }
            }
        }
    }

    @Test
    fun power() {
        val monoid = ringUInt.toMultiplicativeMonoid()
        for (x in 0u..100u) {
            for (power in 0u..100u) {
                val powerM = x.powerM(power)
                val powerS = x.powerSq(power)
                val powerRM = monoid.powerM(x, power)
                val powerRS = monoid.powerS(x, power)
//                println("$x^$power = $powerM, $powerS")
                assertEquals(powerM, powerS)
                assertEquals(powerRM, powerRS)
                assertEquals(powerM, powerRM)
            }
        }
    }

}