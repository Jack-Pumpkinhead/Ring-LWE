package math.coding.permutation

/**
 * Created by CowardlyLion at 2022/2/12 18:23
 */
class InversePermutation(val permutation: Permutation) : Permutation(permutation.size) {

    override operator fun invoke(x: UInt): UInt = permutation.inv(x)

    override fun inv(y: UInt): UInt = permutation(y)

    override fun iterator(): Iterator<PermutationPair> {
        val iterator = permutation.iterator()
        return object : Iterator<PermutationPair> {
            override fun hasNext(): Boolean = iterator.hasNext()
            override fun next(): PermutationPair {
                val (source, target) = iterator.next()
                return PermutationPair(target, source)
            }
        }
    }

    override val inverse: Permutation
        get() = permutation
}