package cryptography.lattice.ring_lwe

import math.random.randomizedElements
import kotlin.random.Random

/**
 * Created by CowardlyLion at 2022/1/12 18:11
 */


/**
 * @return representative of ℝ-coset a+ℤ in [-0.5, 0.5)
 * */
fun centeredRounding(a: Double): Double {
    var r = a.mod(1.0)
    if (r >= 0.5) {
        r -= 1
    }
    return r
}

fun centeredRounding(a: List<Double>) = a.map { centeredRounding(it) }

/**
 * @return representative of ℤ-coset a+mℤ in [-m/2, m/2)
 * */
fun integerCenteredRounding(a: Int, modulus: UInt): Int {
    require(modulus.toInt() > 0)
    var r = a.mod(modulus.toInt())
    if (r >= (modulus.toInt() + 1) / 2) {
        r -= modulus.toInt()
    }
    return r
}

fun integerCenteredRounding(a: List<Int>, modulus: UInt) = a.map { integerCenteredRounding(it, modulus) }

fun Random.randomizedRounding(a: Double): Double {
    val r = a.mod(1.0)
    return randomizedElements(doubleArrayOf(r, 1 - r), r - 1, r)
}

fun Random.randomizedRounding(coordinate: List<Double>) = coordinate.map { this@randomizedRounding.randomizedRounding(it) }