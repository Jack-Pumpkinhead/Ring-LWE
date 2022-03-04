package cryptography.lattice.ring_lwe.public_key_cryptosystem

import kotlinx.coroutines.runBlocking
import math.complex_number.FieldComplexNumberDouble
import math.complex_number.ComplexNumber
import math.complex_number.maxRoundingError
import math.martix.AbstractMatrix
import math.operation.matrixApproximatelyEquals
import math.operation.matrixToStringComplexDouble
import math.random.randomMatrix
import math.statistic.TaskTimingStatistic
import math.timing.EqualTaskTiming
import math.timing.Task
import math.timing.TwoMatrix
import math.timing.twoMatrixMultiplicationTasks
import org.junit.jupiter.api.Test

/**
 * Created by CowardlyLion at 2022/2/24 22:04
 */
internal class ConjugateMirrorMatrixComplexDoubleTest {

    @Test
    fun print() {
        for (i in 1u..10u) {
            val matrix = ConjugateMirrorMatrixComplexDouble(2u * i, i)
            println(matrixToStringComplexDouble(matrix))
        }
    }

    fun base(range: UIntRange) {
        runBlocking {
            val tasks = object : EqualTaskTiming<TwoMatrix<ComplexNumber<Double>>, AbstractMatrix<ComplexNumber<Double>>> {
                override fun equals(a: AbstractMatrix<ComplexNumber<Double>>, b: AbstractMatrix<ComplexNumber<Double>>): Boolean = matrixApproximatelyEquals(a, b)
                override val tasks: List<Task<TwoMatrix<ComplexNumber<Double>>, AbstractMatrix<ComplexNumber<Double>>>> = twoMatrixMultiplicationTasks()
            }
            val statistic = TaskTimingStatistic(tasks)

            for (i in range) {
                val matrix = ConjugateMirrorMatrixComplexDouble(2u * i, i)
                repeat(2) {
                    val x = FieldComplexNumberDouble.randomMatrix(matrix.columns, 10u, 100.0)
                    statistic.go(TwoMatrix(matrix, x))
                }
            }
            statistic.printAverageAndStandardDeviation()
            println("range: $range")
            println("maxError: $maxRoundingError")
        }
    }

    // *  : average 0.05692ms, deviation 0.08013ms
    // *p : average 0.04767ms, deviation 0.03299ms
    // * t: average 0.10708ms, deviation 0.33965ms
    // *pt: average 0.08894ms, deviation 0.10209ms
    //d*  : average 4.41728ms, deviation 3.52413ms
    //samples: 200, total time: 943.576400ms
    //range: 1..100
    //maxError: 2.8421709430404007E-14
    @Test
    fun multiplication() {
        base(1u..100u)
    }

    // *  : average 0.23871ms, deviation 0.21026ms
    // *p : average 0.14794ms, deviation 0.14967ms
    // * t: average 0.26782ms, deviation 0.39887ms
    // *pt: average 0.24576ms, deviation 0.22949ms
    //d*  : average 78.33853ms, deviation 17.78474ms
    //samples: 202, total time: 16.006230900s
    //range: 200..300
    //maxError: 2.8421709430404007E-14
    @Test
    fun multiplication1() {
        base(200u..300u)
    }


}