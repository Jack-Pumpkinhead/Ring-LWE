package math.integer

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.math.sqrt

/**
 * Created by CowardlyLion at 2022/1/5 19:24
 */

private val mutex = Mutex()

/**
 * can store prime from 2 up to 50685770167 (the largest prime)
 */
private val primeCache = mutableListOf(2uL, 3uL, 5uL)

private var nextPrimeIndex = 2
private var nextPrime = primeCache[nextPrimeIndex]

private val chunk = mutableListOf(1uL, 5uL)
private var chunkWidth = 2uL * 3uL
private val nextChunk = mutableListOf(1uL)
private var nextChunkWidth = chunkWidth * nextPrime

private var start = chunkWidth

private var eulerSieveIndex = 1
var toRemove = nextPrime * chunk[eulerSieveIndex]
var lastRemovedIndex = nextPrimeIndex

private fun searchOnce() {
//    println("primes: ${primeCache.size}, last prime: ${primeCache.last()}")
    if (start == nextChunkWidth) {
        chunk.clear()
        chunk += nextChunk
        chunkWidth = nextChunkWidth
        nextPrimeIndex++
        nextPrime = primeCache[nextPrimeIndex]
        nextChunkWidth *= nextPrime

        nextChunk.removeAt(1)      //index of next prime at next chunk is 1
        lastRemovedIndex = 1
        eulerSieveIndex = 1
        toRemove = nextPrime * chunk[eulerSieveIndex]       //if nextChunk used as coefficient of nextPrime, then it is bug.
        while (toRemove < chunkWidth) {
//            println("\tsearch: $toRemove, from: $lastRemovedIndex")
//            println("\tchunk: ${chunk.joinToString()}")
//            println("\tnextChunk: ${nextChunk.joinToString()}")
            val search = nextChunk.binarySearch(toRemove, fromIndex = lastRemovedIndex)
            if (search >= 0) {            //different removed element from different prime may overlap
                nextChunk.removeAt(search)
                lastRemovedIndex = search
            }
            eulerSieveIndex++
            toRemove = nextPrime * chunk[eulerSieveIndex]
        }
    }
    val startIndex = nextChunk.size
    for (a in chunk) {
        nextChunk += start + a
    }
    val end = start + chunkWidth

//    println("before toRemove: $toRemove")
    while (toRemove < end) {
//        println("search: $toRemove, from: $lastRemovedIndex")
//        println("chunk: ${chunk.joinToString()}")
//        println("nextChunk: ${nextChunk.joinToString()}")
//        println()
        val search = nextChunk.binarySearch(toRemove, fromIndex = lastRemovedIndex)
        if (search >= 0) {                //different removed element from different prime may overlap
            nextChunk.removeAt(search)
            lastRemovedIndex = search
        }
        eulerSieveIndex++
        if (eulerSieveIndex == chunk.size) break    //may overflow at last small chunk, so detect it.
        toRemove = nextPrime * chunk[eulerSieveIndex]
    }

//    println("after toRemove: $toRemove")
//    println("after chunk: ${chunk.joinToString()}")
//    println("after nextChunk: ${nextChunk.joinToString()}")

    val chunkShift = mutableListOf<ULong>()
    for (i in startIndex until nextChunk.size) {
        chunkShift += nextChunk[i]
    }

    val sqrtEnd = sqrt(end.toDouble()).toULong()
    var lastIndexP = eulerSieveIndex - 1
    for (i in nextPrimeIndex + 1 until primeCache.size) {
        val p = primeCache[i]
        if (p > sqrtEnd) break

        var toRemoveP = p * chunk[lastIndexP]
        while (toRemoveP >= end) {
            lastIndexP--
            toRemoveP = p * chunk[lastIndexP]
        }

        var indexP = lastIndexP
        var lastRemovedIndexP = chunkShift.size
        while (toRemoveP in start until end) {
            val search = chunkShift.binarySearch(toRemoveP, toIndex = lastRemovedIndexP)
            if (search >= 0) {              //different removed element from different prime may overlap
                lastRemovedIndexP = search
                chunkShift.removeAt(search)
            }
            indexP--
            toRemoveP = p * chunk[indexP]
        }
    }

//    println("start: $start, end: $end")
    primeCache += chunkShift
    start += chunkWidth
}

/**
 * fast prime-generating algorithm based on wheel factorization and euler sieve.
 * thread safe
 */
suspend fun primeOf(n: Int): ULong {
    return mutex.withLock {
        while (n >= primeCache.size) {
            searchOnce()
        }
        primeCache[n]
    }
}