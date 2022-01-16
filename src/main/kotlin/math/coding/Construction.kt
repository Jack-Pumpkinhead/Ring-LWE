package math.coding

import com.ionspin.kotlin.bignum.integer.BigInteger
import math.abstract_structure.instance.ringBigInteger
import math.operations.product

/**
 * Created by CowardlyLion at 2022/1/16 20:12
 */

//require bound to be pair-wise coprime and >1

private fun chineseRemainderToLadder(bounds: List<BigInteger>): SwitchIndexPermutation = object : SwitchIndexPermutation(ringBigInteger.product(bounds)) {
    override val fromIndex = ChineseRemainderIndex(bounds)
    override val toIndex = LadderIndex(bounds)
}

private fun ladderToRuritanian(bounds: List<BigInteger>): SwitchIndexPermutation = object : SwitchIndexPermutation(ringBigInteger.product(bounds)) {
    override val fromIndex = LadderIndex(bounds)
    override val toIndex = RuritanianIndex(bounds)
}

private fun ruritanianToChineseRemainder(bounds: List<BigInteger>): SwitchIndexPermutation = object : SwitchIndexPermutation(ringBigInteger.product(bounds)) {
    override val fromIndex = RuritanianIndex(bounds)
    override val toIndex = ChineseRemainderIndex(bounds)
}

private fun ladderToChineseRemainder(bounds: List<BigInteger>): SwitchIndexPermutation = object : SwitchIndexPermutation(ringBigInteger.product(bounds)) {
    override val fromIndex = LadderIndex(bounds)
    override val toIndex = ChineseRemainderIndex(bounds)
}

private fun ruritanianToLadder(bounds: List<BigInteger>): SwitchIndexPermutation = object : SwitchIndexPermutation(ringBigInteger.product(bounds)) {
    override val fromIndex = RuritanianIndex(bounds)
    override val toIndex = LadderIndex(bounds)
}

private fun chineseRemainderToRuritanian(bounds: List<BigInteger>): SwitchIndexPermutation = object : SwitchIndexPermutation(ringBigInteger.product(bounds)) {
    override val fromIndex = ChineseRemainderIndex(bounds)
    override val toIndex = RuritanianIndex(bounds)
}


//LC^-1
fun permLCInv(bounds: List<BigInteger>) = chineseRemainderToLadder(bounds)
fun permCLInv(bounds: List<BigInteger>) = ladderToChineseRemainder(bounds)
fun permRLInv(bounds: List<BigInteger>) = ladderToRuritanian(bounds)
fun permLRInv(bounds: List<BigInteger>) = ruritanianToLadder(bounds)
fun permCRInv(bounds: List<BigInteger>) = ruritanianToChineseRemainder(bounds)
fun permRCInv(bounds: List<BigInteger>) = chineseRemainderToRuritanian(bounds)
