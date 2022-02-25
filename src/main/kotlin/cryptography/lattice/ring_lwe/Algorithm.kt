package cryptography.lattice.ring_lwe

import kotlin.random.Random

/**
 * Created by CowardlyLion at 2022/1/12 18:11
 */


/**
 * return representative of ℝ-coset [a]+ℤ in [-1/2, 1/2)
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
 * return representative of ℤ-coset [a]+mℤ in [-m/2, m/2)
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

/**
 * sample r-1, r with probability r, 1-r
 *
 * has expectation 0
 *
 * require 0 <= [r] < 1
 */
fun Random.randomlySelectNearZero(r: Double): Double = if (nextDouble() < r) r - 1 else r

/**
 * i = x.toLong()
 * r = x - i
 */
fun Random.specialProbabilityOfMinusOne(x: Double, i:Long): Boolean = nextDouble() < x - i.toDouble()

fun Random.modOneThenRandomlySelectNearZero(coordinate: List<Double>) = coordinate.map { this@modOneThenRandomlySelectNearZero.randomlySelectNearZero(it.mod(1.0)) }
