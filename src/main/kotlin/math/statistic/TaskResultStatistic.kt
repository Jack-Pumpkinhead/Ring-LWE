package math.statistic

import math.timing.TaskTiming

/**
 * Created by CowardlyLion at 2022/2/2 18:51
 */
class TaskResultStatistic<Condition, Result>(val timing: TaskTiming<Condition, Result>) {

    val resultStatistic = List<MutableList<Result>>(timing.tasks.size) { mutableListOf() }

    var samples = 0

    suspend fun go(condition: Condition) {
        val results = timing.go(condition)
        for (i in results.indices) {
            resultStatistic[i] += results[i].result
        }
        samples++
    }

}