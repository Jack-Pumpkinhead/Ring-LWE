package math.statistic

import com.ionspin.kotlin.bignum.integer.BigInteger


/**
 * Created by CowardlyLion at 2022/2/2 18:57
 */
fun List<UInt>.countingUInt(): List<MultipleObject<UInt>> {
    val map = mutableMapOf<UInt, MultipleObject<UInt>>()
    for (i in this) {
        val mi = map[i]
        if (mi != null) {
            map[i] = mi.copy(i, mi.multiple + 1u)
        } else {
            map[i] = MultipleObject(i, 1u)
        }
    }
    return map.values.toList()
}

fun List<BigInteger>.countingBigInteger(): List<MultipleObject<BigInteger>> {
    val map = mutableMapOf<BigInteger, MultipleObject<BigInteger>>()
    for (i in this) {
        val mi = map[i]
        if (mi != null) {
            map[i] = mi.copy(i, mi.multiple + 1u)
        } else {
            map[i] = MultipleObject(i, 1u)
        }
    }
    return map.values.toList()
}
