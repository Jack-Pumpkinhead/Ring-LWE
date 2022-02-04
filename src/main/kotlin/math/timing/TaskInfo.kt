package math.timing

import kotlin.time.Duration

/**
 * Created by CowardlyLion at 2022/1/27 18:30
 */
class TaskInfo<Condition, Result>(val task: Task<Condition, Result>, condition: Condition, val result: Result, val time: Duration) {

}