package math.integer.uint.factored

/**
 * Created by CowardlyLion at 2022/2/12 20:38
 *
 * represent an UInt with factorization of coprime prime-power factors
 *
 * require [value] >= 1
 */
interface UIntPPPI : AbstractFactoredUInt {

    override val factors: List<UIntPPI>

    val eulerTotient: UInt

    val radical: UInt

    /**
     * require 0 <= [i] < [eulerTotient]
     */
    fun coprimeNumberAtUnsafe(i: UInt): UInt

}