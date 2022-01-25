package cryptography.lattice.ring_lwe.coding

import com.ionspin.kotlin.bignum.integer.BigInteger
import math.abstract_structure.instance.ringBigInteger
import math.abstract_structure.instance.ringUInt
import math.coding.*
import math.coding.permutation.BigSwitchIndexPermutation
import math.coding.permutation.SwitchIndexPermutation
import math.operation.product


/**
 * Created by CowardlyLion at 2022/1/16 20:12
 */

//require bound to be pair-wise coprime and >1

//LC^-1
fun permLCInv(bounds: List<BigInteger>): BigSwitchIndexPermutation {
    val size = ringBigInteger.product(bounds)
    return BigSwitchIndexPermutation(BigChineseRemainderIndex(bounds, size), BigLadderIndex(bounds, size))
}

fun permCLInv(bounds: List<BigInteger>): BigSwitchIndexPermutation {
    val size = ringBigInteger.product(bounds)
    return BigSwitchIndexPermutation(BigLadderIndex(bounds, size), BigChineseRemainderIndex(bounds, size))
}

fun permRLInv(bounds: List<BigInteger>): BigSwitchIndexPermutation {
    val size = ringBigInteger.product(bounds)
    return BigSwitchIndexPermutation(BigLadderIndex(bounds, size), BigRuritanianIndex(bounds, size))
}

fun permLRInv(bounds: List<BigInteger>): BigSwitchIndexPermutation {
    val size = ringBigInteger.product(bounds)
    return BigSwitchIndexPermutation(BigRuritanianIndex(bounds, size), BigLadderIndex(bounds, size))
}

fun permCRInv(bounds: List<BigInteger>): BigSwitchIndexPermutation {
    val size = ringBigInteger.product(bounds)
    return BigSwitchIndexPermutation(BigRuritanianIndex(bounds, size), BigChineseRemainderIndex(bounds, size))
}

fun permRCInv(bounds: List<BigInteger>): BigSwitchIndexPermutation {
    val size = ringBigInteger.product(bounds)
    return BigSwitchIndexPermutation(BigChineseRemainderIndex(bounds, size), BigRuritanianIndex(bounds, size))
}

//require bound to be pair-wise coprime and >1

//LC^-1
fun permLCInv(bounds: List<UInt>): SwitchIndexPermutation {
    val size = ringUInt.product(bounds)
    return SwitchIndexPermutation(ChineseRemainderIndex(bounds, size), LadderIndex(bounds, size))
}

fun permCLInv(bounds: List<UInt>): SwitchIndexPermutation {
    val size = ringUInt.product(bounds)
    return SwitchIndexPermutation(LadderIndex(bounds, size), ChineseRemainderIndex(bounds, size))
}

fun permRLInv(bounds: List<UInt>): SwitchIndexPermutation {
    val size = ringUInt.product(bounds)
    return SwitchIndexPermutation(LadderIndex(bounds, size), RuritanianIndex(bounds, size))
}

fun permLRInv(bounds: List<UInt>): SwitchIndexPermutation {
    val size = ringUInt.product(bounds)
    return SwitchIndexPermutation(RuritanianIndex(bounds, size), LadderIndex(bounds, size))
}

fun permCRInv(bounds: List<UInt>): SwitchIndexPermutation {
    val size = ringUInt.product(bounds)
    return SwitchIndexPermutation(RuritanianIndex(bounds, size), ChineseRemainderIndex(bounds, size))
}

fun permRCInv(bounds: List<UInt>): SwitchIndexPermutation {
    val size = ringUInt.product(bounds)
    return SwitchIndexPermutation(ChineseRemainderIndex(bounds, size), RuritanianIndex(bounds, size))
}

