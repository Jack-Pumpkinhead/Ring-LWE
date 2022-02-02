package math.statistic

/**
 * Created by CowardlyLion at 2022/2/2 18:57
 */
fun List<UInt>.counting(): List<MultipleObject<UInt>> {
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