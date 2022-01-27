package math.timing

import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

/**
 * Created by CowardlyLion at 2022/1/27 18:26
 */
class Task<Condition, A>(val info: String, val work: suspend (Condition) -> A) {

    @OptIn(ExperimentalTime::class)
    suspend fun go(condition: Condition): TaskResult<Condition, A> {
        val result: A
        val time = measureTime {
            result = work(condition)
        }
        return TaskResult(this, condition, result, time)
    }

}