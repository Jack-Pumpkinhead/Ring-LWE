package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform

import math.abstract_structure.Ring
import math.martix.AbstractColumnVector
import util.stdlib.lazyAssert2

/**
 * Created by CowardlyLion at 2022/2/3 22:34
 *
 * x0, 0, ..., 0, x1, x2, ...
 *
 * padding zero at position 1 to match [size]
 */
class DftPaddingZeroColumnVector<A>(override val ring: Ring<A>, val list: List<A>, override val size: UInt) : AbstractColumnVector<A> {

    init {
        lazyAssert2 {
            assert(size > list.size.toUInt())
        }
    }

    val indexOfLastZero = size - list.size.toUInt()

    override fun vectorElementAtUnsafe(index: UInt): A =
        if (index == 0u) {
            list[0]
        } else if (index <= indexOfLastZero) {
            ring.zero
        } else {
            list[(index - indexOfLastZero).toInt()]
        }

}