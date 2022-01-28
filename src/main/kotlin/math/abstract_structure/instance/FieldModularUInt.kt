package math.abstract_structure.instance

import cryptography.lattice.ring_lwe.matrix.RootDataUInt
import cryptography.lattice.ring_lwe.matrix.RootDataUIntPrime
import cryptography.lattice.ring_lwe.matrix.RootDataUIntPrimePower
import kotlinx.coroutines.runBlocking
import math.abstract_structure.Field
import math.integer.FactorizationUInt
import math.integer.FactorizationUIntPrime
import math.integer.FactorizationUIntPrimePower
import math.integer.firstMultiplicativeGeneratorOfPrimeFieldUnsafe
import math.integer.modular.ModularUInt
import math.integer.modular.toUIntModular

/**
 * Created by CowardlyLion at 2022/1/26 23:17
 */
class FieldModularUInt(val prime: UInt) : RingModularUInt(prime), Field<ModularUInt> {

    override val descriptions: MutableSet<String> = mutableSetOf("field of integer modulo $prime")

    override fun hasInverse(a: ModularUInt): Boolean = true

    val firstGenerator: ModularUInt by lazy {
        runBlocking {
            firstMultiplicativeGeneratorOfPrimeFieldUnsafe(prime).toUIntModular(prime)
        }
    }

    /**
     * precomputed [primeMinusOne]
     */
    fun firstMainRootData(primeMinusOne: FactorizationUInt): RootDataUInt<ModularUInt> = RootDataUInt(this, firstGenerator, primeMinusOne)

    fun rootData(order: FactorizationUInt, primeMinusOne: FactorizationUInt): RootDataUInt<ModularUInt> = firstMainRootData(primeMinusOne).subroot(order)
    fun rootData(order: FactorizationUIntPrimePower, primeMinusOne: FactorizationUInt): RootDataUIntPrimePower<ModularUInt> = firstMainRootData(primeMinusOne).subroot(order)
    fun rootData(order: FactorizationUIntPrime, primeMinusOne: FactorizationUInt): RootDataUIntPrime<ModularUInt> = firstMainRootData(primeMinusOne).subroot(order)


}