package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform

import math.integer.operation.modTimesUnsafe
import math.integer.operation.modUnaryMinus

/**
 * Created by CowardlyLion at 2022/1/19 13:56
 */
class PrimeData(val prime: UInt, val generator: UInt) {

    val primeDec = prime - 1u

     val generatorPower: List<UInt>

    init {
        if (prime == 2u) {
            generatorPower = listOf(1u)
        } else {
            val list = mutableListOf(1u, generator)
            var power = generator
            for (i in 2u until primeDec) {
                power = modTimesUnsafe(power, generator, prime)
                list += power
            }
            require(modTimesUnsafe(power, generator, prime) == 1u)
            generatorPower = list
        }
    }

    //if generatorPower eats too much memory, then switch to computing fast power.
    fun generatorPower(i: UInt) = generatorPower[i.toInt()]

    fun inverseGeneratorPower(power: UInt): UInt = generatorPower[modUnaryMinus(power, primeDec).toInt()]


}

