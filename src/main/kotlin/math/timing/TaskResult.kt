package math.timing

import kotlin.time.Duration

/**
 * Created by CowardlyLion at 2022/1/27 18:30
 */
class TaskResult<Condition, A>(val task: Task<Condition, A>, condition: Condition, val result: A, val time: Duration) {

}