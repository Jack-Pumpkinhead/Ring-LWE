package math.integer.uint.modular

import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.DftMatrixPPPI
import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.modular_uint.DftMatrixPPIBuilderModularUInt
import cryptography.lattice.ring_lwe.ring.RootUIntP
import cryptography.lattice.ring_lwe.ring.RootUIntPP
import cryptography.lattice.ring_lwe.ring.RootUIntPPP
import cryptography.lattice.ring_lwe.ring.RootUIntPPPI
import kotlinx.coroutines.runBlocking
import math.abstract_structure.Field
import math.integer.uint.factored.*
import math.integer.uint.firstMultiplicativeGeneratorOfPrimeFieldUnsafe

/**
 * Created by CowardlyLion at 2022/1/26 23:17
 *
 * field of ℤ/p has primitive (p-1)-th root of unity, all non-zero element has order divide (p-1)
 */
class FieldModularUInt(val prime: UInt) : RingModularUInt(prime), Field<ModularUInt> {

    override val descriptions: MutableSet<String> = mutableSetOf("field of integer modulo $prime")

    override fun hasInverse(a: ModularUInt): Boolean = true

    val primeMinusOne: UIntPPPI by lazy {
        runBlocking {
            (prime - 1u).primeFactorization()
        }
    }

    val firstGenerator: RootUIntPPPI<ModularUInt> by lazy {
        val g = firstMultiplicativeGeneratorOfPrimeFieldUnsafe(prime, primeMinusOne)
        when (val order = primeMinusOne) {
            is UIntPPP -> RootUIntPPP(this, g, order)
            is UIntPP  -> RootUIntPP(this, g, order)
            is UIntP   -> RootUIntP(this, g, order)
            else       -> error("unknown type of order $order, class: ${order::class}")
        }
    }

    fun firstFullDftFast(): DftMatrixPPPI<ModularUInt> = DftMatrixPPIBuilderModularUInt.build(firstGenerator)

    override val isExactComputation: Boolean get() = true
}