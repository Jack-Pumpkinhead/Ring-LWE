package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform

import math.abstract_structure.Ring
import math.powerM

/**
 * Created by CowardlyLion at 2022/1/19 14:32
 */
class PrimePowerPrimitiveRootOfUnity<A>(val ring: Ring<A>, val prime: UInt, val power: UInt, val root: A) {

    /**
     * has order [prime]^[power], [prime]^([power]-1), ..., [prime]
     */
    private val rootOfUnities: List<A>

    init {
        val list = mutableListOf(root)
        var rootPower = root
        for (i in 1u until power) {
            rootPower = ring.powerM(rootPower, prime)
            list += rootPower
        }
        rootOfUnities = list
    }

    /**
     * compute primitive [prime]^[powerOrder]-th root of unity
     */
    fun rootOfUnity(powerOrder: UInt): A {
        require(powerOrder > 0u)
        require(powerOrder <= power)
        return rootOfUnities[(power - powerOrder).toInt()]
    }


}