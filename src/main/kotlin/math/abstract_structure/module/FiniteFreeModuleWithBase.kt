package math.abstract_structure.module

import math.abstract_structure.Ring
import math.martix.AbstractColumnVector
import math.martix.columnVector

/**
 * Created by CowardlyLion at 2022/1/25 13:56
 */
data class FiniteFreeModuleWithBase<A>(val ring: Ring<A>, val base: String, override val dimension: UInt) : FiniteFreeModule<A, AbstractColumnVector<A>> {

    override val descriptions: MutableSet<String> = ring.descriptions.mapTo(mutableSetOf()) { "free module of dimension $dimension over {$it} with base $base" }

    override val zero: AbstractColumnVector<A> get() = ring.columnVector(dimension) { ring.zero }

    override fun add(x: AbstractColumnVector<A>, y: AbstractColumnVector<A>): AbstractColumnVector<A> = ring.columnVector(dimension) { i -> ring.add(x[i], y[i]) }

    override fun negate(a: AbstractColumnVector<A>): AbstractColumnVector<A> = ring.columnVector(dimension) { i -> ring.negate(a[i]) }

    override fun subtract(x: AbstractColumnVector<A>, y: AbstractColumnVector<A>): AbstractColumnVector<A> = ring.columnVector(dimension) { i -> ring.subtract(x[i], y[i]) }

    override fun action(r: A, a: AbstractColumnVector<A>): AbstractColumnVector<A> = ring.columnVector(dimension) { i -> ring.multiply(r, a[i]) }




}