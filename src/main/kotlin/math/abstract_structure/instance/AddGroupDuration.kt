package math.abstract_structure.instance

import math.abstract_structure.AddGroup
import kotlin.time.Duration

/**
 * Created by CowardlyLion at 2022/1/27 18:56
 */
object AddGroupDuration : AddGroup<Duration> {
    override val descriptions: MutableSet<String> = mutableSetOf("additive group of Duration")
    override val zero: Duration = Duration.ZERO
    override fun add(x: Duration, y: Duration): Duration = x + y
    override fun negate(a: Duration): Duration = -a
}