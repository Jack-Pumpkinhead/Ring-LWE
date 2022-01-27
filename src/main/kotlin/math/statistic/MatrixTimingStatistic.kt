package math.statistic

import math.abstract_structure.instance.RingUInt
import math.martix.AbstractMatrix
import math.martix.zeroMutableMatrix
import math.timing.EqualTaskTiming

/**
 * Created by CowardlyLion at 2022/1/27 21:47
 */
object MatrixTimingStatistic {

    val timing = EqualTaskTiming<Pair<AbstractMatrix<UInt>, AbstractMatrix<UInt>>, AbstractMatrix<UInt>>(false)

    init {
        timing.registerTask(" *  ") { (m, x) -> m * x }
        timing.registerTask(" *p ") { (m, x) -> m.timesRowParallel(x) }
        timing.registerTask(" * t") { (m, x) ->
            val result = RingUInt.zeroMutableMatrix(m.rows, x.columns)
            m.multiplyTo(x, result)
            result
        }
        timing.registerTask(" *pt") { (m, x) ->
            val result = RingUInt.zeroMutableMatrix(m.rows, x.columns)
            m.multiplyToRowParallel(x, result)
            result
        }

        timing.registerTask("d*  ") { (m, x) -> math.operation.multiply(m, x) }
        timing.registerTask("d*p ") { (m, x) -> math.operation.multiplyRowParallel(m, x) }
        timing.registerTask("d* t") { (m, x) ->
            val result = RingUInt.zeroMutableMatrix(m.rows, x.columns)
            math.operation.multiplyTo(m, x, result)
            result
        }
        timing.registerTask("d*pt") { (m, x) ->
            val result = RingUInt.zeroMutableMatrix(m.rows, x.columns)
            math.operation.multiplyToRowParallel(m, x, result)
            result
        }
    }

    val statistic = RepeatTaskStatistic(timing)

}