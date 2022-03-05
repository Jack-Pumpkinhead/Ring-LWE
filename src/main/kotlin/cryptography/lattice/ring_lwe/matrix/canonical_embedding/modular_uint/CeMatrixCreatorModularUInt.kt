package cryptography.lattice.ring_lwe.matrix.canonical_embedding.modular_uint

import cryptography.lattice.ring_lwe.ring.root.RootCalculatorUnsafeModularUInt
import math.integer.uint.modular.FieldModularUInt
import math.integer.uint.modular.ModularUInt
import util.mapper.FactorizationToRoot2ToCe

/**
 * Created by CowardlyLion at 2022/3/4 23:38
 */
object CeMatrixCreatorModularUInt : FactorizationToRoot2ToCe<FieldModularUInt, UInt, ModularUInt>(RootCalculatorUnsafeModularUInt, CeMatrixBuilderModularUInt)
