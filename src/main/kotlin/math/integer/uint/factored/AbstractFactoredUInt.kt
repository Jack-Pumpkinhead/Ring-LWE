package math.integer.uint.factored

/**
 * Created by CowardlyLion at 2022/2/12 20:22
 *
 * represent a UInt with a random multiplicative factorization
 *
 * require [value] > 1
 */
interface AbstractFactoredUInt {

    val value: UInt

    val factors: List<AbstractFactoredUInt>

}