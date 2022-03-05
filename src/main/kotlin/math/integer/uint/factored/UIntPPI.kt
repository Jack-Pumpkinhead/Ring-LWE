package math.integer.uint.factored

/**
 * Created by CowardlyLion at 2022/2/12 20:39
 *
 * represent a UInt in the form [prime]^[power] with [prime] prime, [power]>=0
 */
interface UIntPPI : UIntPPPI {

    val prime: UInt

    val power: UInt

    override val factors: List<UIntPPI>
        get() = listOf(this)

    override val radical: UInt get() = prime

    override fun coprimeNumberAtUnsafe(i: UInt): UInt = (i / (prime - 1u)) + i + 1u

    fun prime(): UIntP = UIntP(prime)

}