package cryptography.lattice.ring_lwe.category

import math.abstract_structure.instance.FieldComplexNumberDouble
import math.abstract_structure.instance.FieldDouble
import math.abstract_structure.module.category.ModuleCategories
import math.integer.long.RingLong
import math.integer.uint.RingUInt

/**
 * Created by CowardlyLion at 2022/2/27 22:37
 */
val complexModule = ModuleCategories.categoryOf(FieldComplexNumberDouble)
val realModule = ModuleCategories.categoryOf(FieldDouble)
val integerModule = ModuleCategories.categoryOf(RingUInt)
val longModule = ModuleCategories.categoryOf(RingLong)