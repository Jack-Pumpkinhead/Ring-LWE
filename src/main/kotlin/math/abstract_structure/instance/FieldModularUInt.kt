package math.abstract_structure.instance

import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.DftMatrix
import cryptography.lattice.ring_lwe.ring.RootDataUInt
import kotlinx.coroutines.runBlocking
import math.abstract_structure.Field
import math.integer.FactorizationUInt
import math.integer.firstMultiplicativeGeneratorOfPrimeFieldUnsafe
import math.integer.modular.ModularUInt
import math.integer.primeFactorization

/**
 * Created by CowardlyLion at 2022/1/26 23:17
 */
class FieldModularUInt(val prime: UInt) : RingModularUInt(prime), Field<ModularUInt> {

    override val descriptions: MutableSet<String> = mutableSetOf("field of integer modulo $prime")

    override fun hasInverse(a: ModularUInt): Boolean = true

    val primeMinusOne: FactorizationUInt by lazy {
        runBlocking {
            (prime - 1u).primeFactorization()
        }
    }

    val firstGenerator: RootDataUInt<ModularUInt> by lazy {
        val g = firstMultiplicativeGeneratorOfPrimeFieldUnsafe(prime, primeMinusOne)
        RootDataUInt(this, g, primeMinusOne)
    }

    /**
     * field of ℤ/p has primitive (p-1)-th root of unity, all multiplicative element has order divide (p-1)
     */
    fun firstFullDft(): DftMatrix<ModularUInt> = DftMatrix(firstGenerator)



}