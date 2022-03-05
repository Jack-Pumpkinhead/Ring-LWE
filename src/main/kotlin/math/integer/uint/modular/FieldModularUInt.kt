package math.integer.uint.modular

import cryptography.lattice.ring_lwe.ring.RootPImpl
import cryptography.lattice.ring_lwe.ring.RootPPImpl
import cryptography.lattice.ring_lwe.ring.RootPPPI
import cryptography.lattice.ring_lwe.ring.RootPPPImpl
import kotlinx.coroutines.runBlocking
import math.abstract_structure.Field
import math.abstract_structure.monoid.MonoidElementCachePowerImpl
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

    val firstGenerator: RootPPPI<ModularUInt> by lazy {
        val g = firstMultiplicativeGeneratorOfPrimeFieldUnsafe(prime, primeMinusOne)
        val g1 = MonoidElementCachePowerImpl(this, g, primeMinusOne.value)
        when (val order = primeMinusOne) {
            is UIntPPP -> RootPPPImpl(this, g1, order)
            is UIntPP  -> RootPPImpl(this, g1, order)
            is UIntP   -> RootPImpl(this, g1, order)
            else       -> error("unknown type of order $order, class: ${order::class}")
        }
    }

    override val isExactComputation: Boolean get() = true
}