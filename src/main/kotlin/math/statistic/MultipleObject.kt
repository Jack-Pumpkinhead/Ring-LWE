package math.statistic

/**
 * Created by CowardlyLion at 2022/2/2 18:58
 */
data class MultipleObject<A>(val value: A, val multiple: UInt){
    override fun toString(): String = "$value (x$multiple)"
}
