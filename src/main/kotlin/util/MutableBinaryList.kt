package util

import util.bit.bitAt
import util.bit.copyWithBitChange
import util.stdlib.toULong

/**
 * Created by CowardlyLion at 2022/2/9 11:53
 *
 * can only store bits less than 64 * Int.MAX_VALUE
 *
 * bits that stored always >= [size]
 */
class MutableBinaryList(var size: ULong, val list: MutableList<ULong> = mutableListOf()) {

    init {
        if (size > 0uL) {
            require((size - 1uL) / 64uL <= list.size.toULong())
        }
    }

    fun isEmpty() = size == 0uL
    fun isNotEmpty() = size > 0uL

    operator fun get(i: ULong): Boolean {
        require(i < size)
        return bitAtUnsafe(i)
    }

    operator fun get(i: UInt): Boolean {
        require(i < size)
        return bitAtUnsafe(i)
    }

    operator fun set(i: ULong, bit: Boolean) {
        require(i < size)
        setUnsafe(i, bit)
    }

    operator fun set(i: UInt, bit: Boolean) {
        require(i < size)
        setUnsafe(i, bit)
    }

    operator fun plusAssign(bit: Boolean) {
        val wordIndex = size / 64uL
        require(wordIndex <= Int.MAX_VALUE.toULong())
        if (wordIndex.toInt() >= list.size) {
            list += bit.toULong()
        } else {
            list[wordIndex.toInt()] = list[wordIndex.toInt()].copyWithBitChange(size.mod(64u), bit)
        }
        size++
    }

    private fun bitAtUnsafe(i: ULong): Boolean = list[(i / 64uL).toInt()].bitAt(i.mod(64u))

    private fun bitAtUnsafe(i: UInt): Boolean = list[(i / 64u).toInt()].bitAt(i.mod(64u))

    private fun setUnsafe(i: ULong, bit: Boolean) {
        val wordIndex = i / 64uL
        require(wordIndex <= Int.MAX_VALUE.toULong())
        list[wordIndex.toInt()] = list[wordIndex.toInt()].copyWithBitChange(i.mod(64u), bit)
    }

    private fun setUnsafe(i: UInt, bit: Boolean) {
        val wordIndex = (i / 64u).toInt()   //always <= Int.MAX_VALUE
        list[wordIndex] = list[wordIndex].copyWithBitChange(i.mod(64u), bit)
    }


}