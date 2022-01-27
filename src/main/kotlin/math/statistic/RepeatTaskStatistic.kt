package math.statistic

import math.timing.EqualTaskTiming
import kotlin.math.sqrt
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.time.toDuration

/**
 * Created by CowardlyLion at 2022/1/27 18:47
 */
class RepeatTaskStatistic<Condition, A>(val timing: EqualTaskTiming<Condition, A>) {

    //    not record conditions to save space
    //    val conditions = mutableListOf<Condition>()
    val statistics = List<MutableList<Duration>>(timing.tasks.size) { mutableListOf() }
    val totalTime = MutableList(timing.tasks.size) { Duration.ZERO }
    var size = 0

    suspend fun go(condition: Condition) {
        val results = timing.go(condition)
        for (i in results.indices) {
            statistics[i] += results[i].time
            totalTime[i] += results[i].time
        }
        size++
    }

    fun average(): List<Duration> {
        return totalTime.map { it / size }
    }

    /**
     * corrected sample standard deviation
     */
    @OptIn(ExperimentalTime::class)
    fun averageAndStandardDeviation(): List<Pair<Duration, Duration>> {
        require(size > 1)
        val averages = average()

        return averages.zip(statistics).map { (average, durations) ->
            var sum = 0.0
            durations.forEach { duration ->
                val difference = duration.toDouble(DurationUnit.MILLISECONDS) - average.toDouble(DurationUnit.MILLISECONDS)
                sum += difference * difference
            }
            average to sqrt(sum / (size - 1)).toDuration(DurationUnit.MILLISECONDS)
        }
    }

    fun printAverageAndStandardDeviation() {
        val info = averageAndStandardDeviation()
        for (i in info.indices) {
            val (average, deviation) = info[i]
            println("${timing.tasks[i].info}: average ${average.toString(DurationUnit.MILLISECONDS, 5)}, deviation ${deviation.toString(DurationUnit.MILLISECONDS, 5)}")
        }
    }


}