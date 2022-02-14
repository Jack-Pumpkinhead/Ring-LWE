package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.modular_uint

import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.DftMatrixPPIBuilder
import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.DftMatrixPI
import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.DftMatrixPPI
import cryptography.lattice.ring_lwe.ring.RootUIntP
import cryptography.lattice.ring_lwe.ring.RootUIntPI
import cryptography.lattice.ring_lwe.ring.RootUIntPPI
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import math.integer.uint.modular.ModularUInt

/**
 * Created by CowardlyLion at 2022/2/13 19:23
 */
object DftMatrixPPIBuilderModularUInt : DftMatrixPPIBuilder<ModularUInt> {

    override val cache = mutableMapOf<RootUIntPPI<ModularUInt>, DftMatrixPPI<ModularUInt>>()

    override fun buildImpl(root: RootUIntPI<ModularUInt>): DftMatrixPI<ModularUInt> = PrimeDftMatrixModularUInt(root)

    val mutex = Mutex()

    suspend fun buildThreadSafe(root: RootUIntP<ModularUInt>) = mutex.withLock { buildImpl(root) }

}