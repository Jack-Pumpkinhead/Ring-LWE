package math.coding.permutation

import com.ionspin.kotlin.bignum.integer.BigInteger
import util.InfiniteBitSet

/**
 * Created by CowardlyLion at 2022/1/16 16:38
 *
 * permutation of {0, 1, ..., size-1}
 */
abstract class BigPermutation(val size: BigInteger) {

    abstract operator fun invoke(x: BigInteger): BigInteger
    abstract fun inv(y: BigInteger): BigInteger

    fun toInverse() = object : BigPermutation(size) {
        override operator fun invoke(x: BigInteger): BigInteger = this@BigPermutation.inv(x)
        override fun inv(y: BigInteger): BigInteger = this@BigPermutation(y)
    }

    /**
     * ONLY capable of size less than Int.MAX_VALUE * 64
     * */
    val cycleDecomposition: List<List<BigInteger>> by lazy {
        val visited = InfiniteBitSet()
        var a = BigInteger.ZERO
        val cycles = mutableListOf<List<BigInteger>>()
        while (a < size) {
            if (visited.bitAt(a)) a++ else {
                visited.setBitAt(a, true)

                val cycle = mutableListOf(a)
                var b = this(a)
                while (b != a) {
                    visited.setBitAt(b, true)
                    cycle += b
                    b = this(b)
                }
                if (cycle.size != 1) {
                    cycles += cycle
                }
                a++
            }
        }
        cycles
    }

    /**
     * ONLY capable of size less than Int.MAX_VALUE * 64
     * */
    val isOddPermutation: Boolean by lazy {
        var result = false
        for (cycle in cycleDecomposition) {
            if (cycle.size % 2 == 0) {
                result = !result
            }
        }
        result
    }

}