package cryptography.lattice.ring_lwe.coding

import com.ionspin.kotlin.bignum.integer.BigInteger
import math.abstract_structure.Ring
import math.coding.*
import math.coding.permutation.BigSwitchIndexPermutation
import math.coding.permutation.SwitchIndexPermutation
import math.integer.big_integer.RingBigInteger
import math.martix.permutationMatrix
import math.operation.product


/**
 * Created by CowardlyLion at 2022/1/16 20:12
 */

//require bound to be pair-wise coprime and >1

//LC^-1
fun permLCInv(bounds: List<BigInteger>): BigSwitchIndexPermutation {
    val size = RingBigInteger.product(bounds)
    return BigSwitchIndexPermutation(BigChineseRemainderIndex(bounds, size), BigLadderIndex(bounds, size))
}

fun permCLInv(bounds: List<BigInteger>): BigSwitchIndexPermutation {
    val size = RingBigInteger.product(bounds)
    return BigSwitchIndexPermutation(BigLadderIndex(bounds, size), BigChineseRemainderIndex(bounds, size))
}

fun permRLInv(bounds: List<BigInteger>): BigSwitchIndexPermutation {
    val size = RingBigInteger.product(bounds)
    return BigSwitchIndexPermutation(BigLadderIndex(bounds, size), BigRuritanianIndex(bounds, size))
}

fun permLRInv(bounds: List<BigInteger>): BigSwitchIndexPermutation {
    val size = RingBigInteger.product(bounds)
    return BigSwitchIndexPermutation(BigRuritanianIndex(bounds, size), BigLadderIndex(bounds, size))
}

fun permCRInv(bounds: List<BigInteger>): BigSwitchIndexPermutation {
    val size = RingBigInteger.product(bounds)
    return BigSwitchIndexPermutation(BigRuritanianIndex(bounds, size), BigChineseRemainderIndex(bounds, size))
}

fun permRCInv(bounds: List<BigInteger>): BigSwitchIndexPermutation {
    val size = RingBigInteger.product(bounds)
    return BigSwitchIndexPermutation(BigChineseRemainderIndex(bounds, size), BigRuritanianIndex(bounds, size))
}

//require bound to be pair-wise coprime and >1

//LC^-1
fun permLCInv(size: UInt, bounds: List<UInt>): SwitchIndexPermutation = SwitchIndexPermutation(ChineseRemainderIndex(bounds, size), LadderIndex(bounds, size))
fun permCLInv(size: UInt, bounds: List<UInt>): SwitchIndexPermutation = SwitchIndexPermutation(LadderIndex(bounds, size), ChineseRemainderIndex(bounds, size))
fun permRLInv(size: UInt, bounds: List<UInt>): SwitchIndexPermutation = SwitchIndexPermutation(LadderIndex(bounds, size), RuritanianIndex(bounds, size))
fun permLRInv(size: UInt, bounds: List<UInt>): SwitchIndexPermutation = SwitchIndexPermutation(RuritanianIndex(bounds, size), LadderIndex(bounds, size))
fun permCRInv(size: UInt, bounds: List<UInt>): SwitchIndexPermutation = SwitchIndexPermutation(RuritanianIndex(bounds, size), ChineseRemainderIndex(bounds, size))
fun permRCInv(size: UInt, bounds: List<UInt>): SwitchIndexPermutation = SwitchIndexPermutation(ChineseRemainderIndex(bounds, size), RuritanianIndex(bounds, size))

fun <A> Ring<A>.permLCInvMatrix(size: UInt, bounds: List<UInt>) = permutationMatrix(permLCInv(size, bounds))
fun <A> Ring<A>.permCLInvMatrix(size: UInt, bounds: List<UInt>) = permutationMatrix(permCLInv(size, bounds))
fun <A> Ring<A>.permRLInvMatrix(size: UInt, bounds: List<UInt>) = permutationMatrix(permRLInv(size, bounds))
fun <A> Ring<A>.permLRInvMatrix(size: UInt, bounds: List<UInt>) = permutationMatrix(permLRInv(size, bounds))
fun <A> Ring<A>.permCRInvMatrix(size: UInt, bounds: List<UInt>) = permutationMatrix(permCRInv(size, bounds))
fun <A> Ring<A>.permRCInvMatrix(size: UInt, bounds: List<UInt>) = permutationMatrix(permRCInv(size, bounds))

