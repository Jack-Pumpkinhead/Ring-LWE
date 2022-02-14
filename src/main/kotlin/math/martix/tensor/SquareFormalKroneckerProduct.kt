package math.martix.tensor

import com.ionspin.kotlin.bignum.integer.toBigInteger
import math.abstract_structure.Ring
import math.integer.big_integer.RingBigInteger
import math.coding.LadderIndex
import math.martix.*
import math.operation.product
import util.stdlib.lazyAssert2

/**
 * Created by CowardlyLion at 2022/1/27 16:40
 */
class SquareFormalKroneckerProduct<A>(override val ring: Ring<A>, override val size: UInt, override val elements: List<AbstractSquareMatrix<A>>) : AbstractSquareFormalProduct<A>, AbstractSquareFormalKroneckerProduct<A> {

    //there are (matrices.size)! ways (permutations) of decomposition, use one that compute m0 first.
    override val matrices: List<AbstractSquareMatrix<A>> =
        when (elements.size) {
            1    -> elements
            else -> {
                var l = size
                var r = 1u

                val result = mutableListOf<AbstractSquareMatrix<A>>()
                for (i in elements.size - 1 downTo 0) {
                    val m = elements[i]
                    l /= m.rows
                    result += SquareWhiskeredKroneckerProduct(ring, size, l, m, r)
                    r *= m.columns
                }
                result
            }
        }

    override val index: LadderIndex = LadderIndex(elements.map { it.size }, size)

    init {
        lazyAssert2 {
            assert(elements.isNotEmpty())
            assert(size.toBigInteger() == RingBigInteger.product(elements.map { it.size.toBigInteger() }))
        }
    }

    override fun elementAtUnsafe(row: UInt, column: UInt): A {
        val row1 = index.decode(row)
        val column1 = index.decode(column)
        return ring.product(0u until elements.size.toUInt()) { i -> elements[i.toInt()].elementAtUnsafe(row1[i.toInt()], column1[i.toInt()]) }
    }

    override fun downCast(): AbstractSquareMatrix<A> = when (elements.size) {
        1    -> elements[0]
        else -> this
    }

    override fun hasInverse(): Boolean = elements.all { it.hasInverse() }

    override val inverse: AbstractSquareMatrix<A> by lazy {
        SquareFormalKroneckerProduct(ring, size, matrices.map { it.inverse })
    }

    override fun determinant(): A {
        TODO("Not yet implemented")
    }

}