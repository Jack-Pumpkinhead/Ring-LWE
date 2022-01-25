package math.coding.permutation

import util.InfiniteBitSet

/**
 * Created by CowardlyLion at 2022/1/22 23:03
 */
abstract class Permutation(val size: UInt) : Iterable<Permutation.PermutationPair> {

    data class PermutationPair(val source: UInt, val target: UInt) {
        override fun toString(): String = "$source -> $target"
    }

    abstract operator fun invoke(x: UInt): UInt
    abstract fun inv(y: UInt): UInt

    fun toInverse() = object : Permutation(size) {
        override operator fun invoke(x: UInt): UInt = this@Permutation.inv(x)
        override fun inv(y: UInt): UInt = this@Permutation(y)
        override fun iterator(): Iterator<PermutationPair> {
            val iterator = this@Permutation.iterator()
            return object : Iterator<PermutationPair> {
                override fun hasNext(): Boolean = iterator.hasNext()
                override fun next(): PermutationPair {
                    val (source, target) = iterator.next()
                    return PermutationPair(target, source)
                }
            }
        }
    }

    /**
     * ONLY capable of size less than Int.MAX_VALUE * 64
     * */
    val cycleDecomposition: List<List<UInt>> by lazy {
        val visited = InfiniteBitSet()
        var a = 0u
        val cycles = mutableListOf<List<UInt>>()
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