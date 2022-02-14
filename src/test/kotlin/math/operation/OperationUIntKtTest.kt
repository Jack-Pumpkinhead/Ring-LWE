package math.operation

import math.integer.uint.modular.RingModularUInt
import math.integer.uint.RingUInt
import math.integer.uint.modular.ModularUInt
import math.integer.uint.modPowerM
import math.integer.uint.modPowerS
import math.integer.uint.powerM
import math.integer.uint.powerS
import math.abstract_structure.algorithm.powerM
import math.abstract_structure.algorithm.powerS
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/**
 * Created by CowardlyLion at 2022/1/4 17:58
 */
internal class OperationUIntKtTest {

    @Test
    fun modPower() {
        for (modulus in 10u..100u) {
            val ringUIntModular = RingModularUInt(modulus)
            for (x in 0u until modulus) {
                for (power in 0u..100u) {
                    val modPowerM = modPowerM(x, power, modulus)
                    val modPowerS = modPowerS(x, power, modulus)
                    val modPowerRM = ringUIntModular.powerM(ModularUInt(modulus, x), power)
                    val modPowerRS = ringUIntModular.powerS(ModularUInt(modulus, x), power)
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
        for (x in 0u..100u) {
            for (power in 0u..100u) {
                val powerM = x.powerM(power)
                val powerS = x.powerS(power)
                val powerRM = RingUInt.powerM(x, power)
                val powerRS = RingUInt.powerS(x, power)
//                println("$x^$power = $powerM, $powerS")
                assertEquals(powerM, powerS)
                assertEquals(powerRM, powerRS)
                assertEquals(powerM, powerRM)
            }
        }
    }

}