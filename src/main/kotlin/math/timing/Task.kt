package math.timing

import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

/**
 * Created by CowardlyLion at 2022/1/27 18:26
 */
class Task<Condition, Result>(val info: String, val work: suspend (Condition) -> Result) {

    @OptIn(ExperimentalTime::class)
    suspend fun goAndMeasureTime(condition: Condition): TaskInfo<Condition, Result> {
        val result: Result
        val time = measureTime {
            result = work(condition)
        }
        return TaskInfo(this, condition, result, time)
    }

}