package cryptography.lattice.ring_lwe.matrix.discrete_fourier_transform

import cryptography.lattice.ring_lwe.ring.*

/**
 * Created by CowardlyLion at 2022/2/13 19:23
 */
interface DftMatrixPPIBuilder<A> {

    val cache: MutableMap<RootUIntPPI<A>, DftMatrixPPI<A>>

    fun buildImpl(root: RootUIntPI<A>): DftMatrixPI<A>

    fun build(root: RootUIntPI<A>) = cache.computeIfAbsent(root) { buildImpl(root) }

    fun build(root: RootUIntPPPI<A>) = when (root) {
        is RootUIntPPP<A> -> DftMatrixPPP(this, root)
        is RootUIntPPI<A> -> build(root)
        else              -> error("unknown type of root $root, class: ${root::class}")
    }

    fun build(root: RootUIntPPI<A>): DftMatrixPPI<A> =
        cache[root] ?: when (root) {
            is RootUIntPP<A> -> {
                val uncalculatedRoot = mutableListOf<RootUIntPPI<A>>(root)
                var subroot = root.subrootReducePowerOne()
                var get1 = cache[subroot]
                while (get1 == null) {
                    uncalculatedRoot += subroot
                    if (subroot is RootUIntPP<A>) {
                        subroot = subroot.subrootReducePowerOne()
                    } else break    //subroot must be a RootUIntP
                    get1 = cache[subroot]
                }

                if (get1 == null) {  //happened when all subroot haven't calculated
                    val primeCase = buildImpl(subroot as RootUIntP<A>)
                    var lastCase: DftMatrixPPI<A> = primeCase
                    cache[subroot] = lastCase
                    for (i in uncalculatedRoot.size - 2 downTo 0) {
                        val root1 = uncalculatedRoot[i] as RootUIntPP<A>
                        lastCase = DftMatrixPP(root1, primeCase, lastCase)
                        cache[root1] = lastCase
                    }
                    lastCase
                } else {
                    val primeCase = when (get1) {
                        is DftMatrixPP -> get1.primeCase
                        is DftMatrixP  -> get1
                        else           -> error("unknown type of matrix $get1, class: ${get1::class}")
                    }
                    var lastCase: DftMatrixPPI<A> = get1
                    for (i in uncalculatedRoot.size - 1 downTo 0) {
                        val root1 = uncalculatedRoot[i] as RootUIntPP<A>
                        lastCase = DftMatrixPP(root1, primeCase, lastCase)
                        cache[root1] = lastCase
                    }
                    lastCase
                }
            }
            is RootUIntP<A>  -> {
                val result = buildImpl(root)
                cache[root] = result
                result
            }
            else             -> error("unknown type of root $root, class: ${root::class}")
        }

}