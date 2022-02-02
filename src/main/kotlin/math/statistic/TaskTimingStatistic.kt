package math.statistic

import math.timing.TaskTiming
import kotlin.math.sqrt
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.time.toDuration

/**
 * Created by CowardlyLion at 2022/1/27 18:47
 */
class TaskTimingStatistic<Condition, Result>(val timing: TaskTiming<Condition, Result>) {

    //    not record conditions to save space
    //    val conditions = mutableListOf<Condition>()
    val timingStatistic = List<MutableList<Duration>>(timing.tasks.size) { mutableListOf() }
    val eachTotalTime = MutableList(timing.tasks.size) { Duration.ZERO }
    var totalTime = Duration.ZERO
    var samples = 0

    suspend fun go(condition: Condition) {
        val results = timing.go(condition)
        for (i in results.indices) {
            timingStatistic[i] += results[i].time
            eachTotalTime[i] += results[i].time
            totalTime += results[i].time
        }
        samples++
    }

    fun average(): List<Duration> {
        return eachTotalTime.map { it / samples }
    }

    /**
     * corrected sample standard deviation
     */
    @OptIn(ExperimentalTime::class)
    fun averageAndStandardDeviation(): List<Pair<Duration, Duration>> {
        require(samples > 1)
        val averages = average()

        return averages.zip(timingStatistic).map { (average, durations) ->
            var sum = 0.0
            durations.forEach { duration ->
                val difference = duration.toDouble(DurationUnit.MILLISECONDS) - average.toDouble(DurationUnit.MILLISECONDS)
                sum += difference * difference
            }
            average to sqrt(sum / (samples - 1)).toDuration(DurationUnit.MILLISECONDS)
        }
    }

    fun printAverageAndStandardDeviation() {
        val info = averageAndStandardDeviation()
        for (i in info.indices) {
            val (average, deviation) = info[i]
            println("${timing.tasks[i].info}: average ${average.toString(DurationUnit.MILLISECONDS, 5)}, deviation ${deviation.toString(DurationUnit.MILLISECONDS, 5)}")
        }
        println("total: $totalTime")
    }


}