package math.timing

import kotlin.time.DurationUnit

/**
 * Created by CowardlyLion at 2022/1/27 18:18
 */
interface EqualTaskTiming<Condition, Result> {

    val tasks: List<Task<Condition, Result>>

    fun equals(a: Result, b: Result): Boolean

    suspend fun goAndPrint(condition: Condition, conditionInfo: String = condition.toString()) {
        if (tasks.isEmpty()) return
        val results = tasks.map { it.go(condition) }

        if (conditionInfo != "") {
            println("condition: $conditionInfo")
        }
        for (result in results) {
            println("${result.task.info}: \t${result.time.toString(DurationUnit.MILLISECONDS, 5)}")
        }

        val a = results[0].result

        println("result: $a")
        if (tasks.size > 1) {
            for (i in 1 until results.size) {
                require(equals(a, results[i].result))
            }
        }

    }

    suspend fun go(condition: Condition): List<TaskResult<Condition, Result>> {
        if (tasks.isEmpty()) return emptyList()
        val results = tasks.map { it.go(condition) }

        val a = results[0].result
        if (tasks.size > 1) {
            for (i in 1 until results.size) {
                require(equals(a, results[i].result))
            }
        }
        return results
    }

}