package math.cache

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Created by CowardlyLion at 2022/1/5 19:24
 */

private val mutex = Mutex()

private val primeCache = mutableListOf(2L, 3L, 5L)

private var chunkWidth = 2L * 3L
private var nextChunkLevel: Int = 2
private val chunk = mutableListOf(1L, 5L)
private var start = chunkWidth

private fun searchOnce() {
    if (start == chunkWidth * primeCache[nextChunkLevel]) {
        val range = chunk.indices
        for (base in chunkWidth until start step chunkWidth) {
            for (i in range) {
                chunk += base + chunk[i]
            }
        }
        val p = primeCache[nextChunkLevel]
        chunk.removeIf { it.mod(p) == 0L }
        chunkWidth = start
        nextChunkLevel++
    }
    chunk.map { start + it }.filterTo(primeCache) {
        for (i in nextChunkLevel until primeCache.size) {
            val p = primeCache[i]
            if (it < p * p) break
            if (it.mod(p) == 0L) return@filterTo false
        }
        true
    }
    start += chunkWidth
}

/**
 * thread safe.
 * @param[n] non-negative number
 * @return n-th prime number
 * */
suspend fun primeOf(n: Int): Long {
    return mutex.withLock {
        while (primeCache.size <= n) {
            searchOnce()
        }
        primeCache[n]
    }
}