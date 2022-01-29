package math.timing

import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

/**
 * Created by CowardlyLion at 2022/1/27 18:26
 */
class Task<Condition, Result>(val info: String, val work: suspend (Condition) -> Result) {

    @OptIn(ExperimentalTime::class)
    suspend fun go(condition: Condition): TaskResult<Condition, Result> {
        val result: Result
        val time = measureTime {
            result = work(condition)
        }
        return TaskResult(this, condition, result, time)
    }

}