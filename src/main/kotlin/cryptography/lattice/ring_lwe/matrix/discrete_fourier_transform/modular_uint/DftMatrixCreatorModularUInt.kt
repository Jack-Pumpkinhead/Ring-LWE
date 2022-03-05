package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform.modular_uint

import cryptography.lattice.ring_lwe.ring.root.RootCalculatorUnsafeModularUInt
import math.integer.uint.modular.FieldModularUInt
import math.integer.uint.modular.ModularUInt
import util.mapper.FactorizationToRoot2ToDft

/**
 * Created by CowardlyLion at 2022/3/4 22:44
 *
 * start from first primitive (p-1)-th root
 *
 * integer DFT with order a
 *
 * x1 for coprime power of root
 */
object DftMatrixCreatorModularUInt : FactorizationToRoot2ToDft<FieldModularUInt, UInt, ModularUInt>(RootCalculatorUnsafeModularUInt, DftMatrixBuilderModularUInt)
