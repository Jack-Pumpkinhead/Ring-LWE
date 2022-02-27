package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.complex_double

import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.DftMatrixPPIBuilder
import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.DftMatrixPI
import cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.DftMatrixPPI
import cryptography.lattice.ring_lwe.ring.RootUIntP
import cryptography.lattice.ring_lwe.ring.RootUIntPI
import cryptography.lattice.ring_lwe.ring.RootUIntPPI
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import math.complex_number.ComplexNumber

/**
 * Created by CowardlyLion at 2022/2/13 19:48
 */
object DftMatrixPPIBuilderComplexDouble : DftMatrixPPIBuilder<ComplexNumber<Double>> {

    //TODO write a reusable cache (indexed only by order)
    override val cache = mutableMapOf<RootUIntPPI<ComplexNumber<Double>>, DftMatrixPPI<ComplexNumber<Double>>>()

    override fun buildImpl(root: RootUIntPI<ComplexNumber<Double>>): DftMatrixPI<ComplexNumber<Double>> = PrimeDftMatrixComplexDouble(root)

    val mutex = Mutex()

    suspend fun buildThreadSafe(root: RootUIntP<ComplexNumber<Double>>) = mutex.withLock { buildImpl(root) }

}