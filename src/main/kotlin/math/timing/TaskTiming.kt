package math.timing

/**
 * Created by CowardlyLion at 2022/2/2 18:30
 */
interface TaskTiming<Condition, Result> {

    val tasks: List<Task<Condition, Result>>

    suspend fun go(condition: Condition): List<TaskResult<Condition, Result>> = tasks.map { it.go(condition) }

}