package math.timing

/**
 * Created by CowardlyLion at 2022/2/2 18:35
 */
class TaskTimingImpl<Condition, Result>(override val tasks: List<Task<Condition, Result>>) : TaskTiming<Condition, Result> {

    constructor(vararg tasks: Task<Condition, Result>) : this(tasks.toList())

}