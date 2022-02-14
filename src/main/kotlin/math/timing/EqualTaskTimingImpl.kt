package math.timing

/**
 * Created by CowardlyLion at 2022/2/13 12:05
 */
class EqualTaskTimingImpl<Condition, Result>(override val tasks: List<Task<Condition, Result>>) : EqualTaskTiming<Condition, Result> {

    constructor(vararg tasks: Task<Condition, Result>) : this(tasks.toList())

    override fun equals(a: Result, b: Result): Boolean = a == b

}