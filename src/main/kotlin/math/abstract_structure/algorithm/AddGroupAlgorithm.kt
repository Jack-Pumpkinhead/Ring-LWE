package math.abstract_structure.algorithm

import math.abstract_structure.AddGroup

/**
 * Created by CowardlyLion at 2022/2/5 13:31
 */

/**
 * Montgomery's ladder for computing multiple of [x] in a monoid A.
 * */
fun <A> AddGroup<A>.multipleM(x: A, multiple: Int): A =
    if (multiple >= 0) {
        multipleM(x, multiple.toUInt())
    } else if (multiple == Int.MIN_VALUE) {
        multipleM(negate(x), Int.MAX_VALUE.toUInt() + 1u)
    } else {
        multipleM(negate(x), (-multiple).toUInt())
    }

fun <A> AddGroup<A>.multipleM(x: A, multiple: Long): A =
    if (multiple >= 0) {
        multipleM(x, multiple.toULong())
    } else if (multiple == Long.MIN_VALUE) {
        multipleM(negate(x), Long.MAX_VALUE.toULong() + 1uL)
    } else {
        multipleM(negate(x), (-multiple).toULong())
    }


/**
 * square version of fast multiple,
 * @return [x]^[multiple]
 * */
fun <A> AddGroup<A>.multipleS(x: A, multiple: Int): A =
    if (multiple >= 0) {
        multipleS(x, multiple.toUInt())
    } else if (multiple == Int.MIN_VALUE) {
        multipleS(negate(x), Int.MAX_VALUE.toUInt() + 1u)
    } else {
        multipleS(negate(x), (-multiple).toUInt())
    }

fun <A> AddGroup<A>.multipleS(x: A, multiple: Long): A =
    if (multiple >= 0) {
        multipleS(x, multiple.toULong())
    } else if (multiple == Long.MIN_VALUE) {
        multipleS(negate(x), Long.MAX_VALUE.toULong() + 1u)
    } else {
        multipleS(negate(x), (-multiple).toULong())
    }

fun <A> AddGroup<A>.multiple(x: A, multiple: Int): A =
    if (multiple >= 0) {
        multiple(x, multiple.toUInt())
    } else if (multiple == Int.MIN_VALUE) {
        multiple(negate(x), Int.MAX_VALUE.toUInt() + 1u)
    } else {
        multiple(negate(x), (-multiple).toUInt())
    }

fun <A> AddGroup<A>.multiple(x: A, multiple: Long): A =
    if (multiple >= 0) {
        multiple(x, multiple.toULong())
    } else if (multiple == Long.MIN_VALUE) {
        multiple(negate(x), Long.MAX_VALUE.toULong() + 1u)
    } else {
        multiple(negate(x), (-multiple).toULong())
    }
