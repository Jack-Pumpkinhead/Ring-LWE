package math.integer.uint.factored

/**
 * Created by CowardlyLion at 2022/2/14 13:41
 *
 * 1 = 2^0
 */
object One : UIntPI {

    override val value: UInt get() = 1u
    override val prime: UInt get() = 2u
    override val power: UInt get() = 0u
    override val eulerTotient: UInt get() = 1u
    override val radical: UInt get() = 1u

    override val factors: List<UIntPPI> get() = emptyList()

    override fun coprimeNumberAtUnsafe(i: UInt): UInt {
        error("no number in [0, 1) coprime to 1")
    }

    override fun toString(): String = value.toString()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is UIntPPPI) return false

        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }

}