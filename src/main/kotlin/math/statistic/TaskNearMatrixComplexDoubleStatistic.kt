package math.statistic

import math.complex_number.ComplexNumber
import math.martix.AbstractMatrix
import math.operation.maxAbsoluteDistance
import math.timing.TaskInfo
import math.timing.TaskTiming

/**
 * Created by CowardlyLion at 2022/2/4 22:55
 */
class TaskNearMatrixComplexDoubleStatistic<Condition>(timing: TaskTiming<Condition, AbstractMatrix<ComplexNumber<Double>>>) : TaskTimingStatistic<Condition, AbstractMatrix<ComplexNumber<Double>>>(timing) {

    val maxDistances: MutableList<Double> = mutableListOf()

    override fun extractInformationFromResults(results: List<TaskInfo<Condition, AbstractMatrix<ComplexNumber<Double>>>>) {
        super.extractInformationFromResults(results)
        var maxDistance = 0.0
        for (i in results.indices) {
            val a = results[i]
            for (j in i + 1 until results.size) {
                val b = results[j]
                val d = maxAbsoluteDistance(a.result, b.result)
                if (d > maxDistance) {
                    maxDistance = d
                }
            }
        }
        maxDistances += maxDistance
    }

    fun printAverageAndMaxDistance() {
        println("average distance: ${maxDistances.average()}, max distance: ${maxDistances.maxOrNull()!!}")
    }
}