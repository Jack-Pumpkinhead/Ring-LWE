package util.stdlib

/**
 * Created by CowardlyLion at 2022/1/19 14:22
 */

fun <R, T> List<T>.runningFoldRight(initial: R, operation: (acc: R, T) -> R): List<R> {
    if (isEmpty()) return listOf(initial)
    val result = ArrayList<R>(size + 1).apply { add(initial) }
    var accumulator = initial
    for (i in this.indices.reversed()) {
        accumulator = operation(accumulator, this[i])
        result.add(accumulator)
    }
    return result.reversed()
}

inline fun <T> list(size: UInt, init: (index: UInt) -> T): List<T> {
    val list = ArrayList<T>(size.toInt())
    for (i in 0u until size) {
        list.add(init(i))
    }
    return list
}