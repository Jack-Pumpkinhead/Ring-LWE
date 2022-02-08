package math.abstract_structure.module

import math.abstract_structure.Ring
import math.martix.AbstractColumnVector
import math.martix.columnVector
import math.martix.concrete.ColumnVector

/**
 * Created by CowardlyLion at 2022/1/25 13:56
 */
data class FreeFiniteModule<R>(val ring: Ring<R>, val base: String, override val dimension: UInt) : AbstractFiniteModule<R, FreeFiniteModuleElement<R>> {

    override val descriptions: MutableSet<String> = ring.descriptions.mapTo(mutableSetOf()) { "free module of dimension $dimension over {$it} with base $base" }

    fun vector(generator: (UInt) -> R): FreeFiniteModuleElement<R> = FreeFiniteModuleElement(this, ring.columnVector(dimension, generator))
    fun vector(list: List<R>): FreeFiniteModuleElement<R> = FreeFiniteModuleElement(this, ColumnVector(ring, list))
    fun vector(vector: AbstractColumnVector<R>): FreeFiniteModuleElement<R> = FreeFiniteModuleElement(this, vector)

    override val zero: FreeFiniteModuleElement<R> get() = vector { ring.zero }

    override fun add(x: FreeFiniteModuleElement<R>, y: FreeFiniteModuleElement<R>): FreeFiniteModuleElement<R> {
        require(x.module == y.module)
        require(x.module == this)
        return vector { i -> ring.add(x[i], y[i]) }
    }

    override fun negate(a: FreeFiniteModuleElement<R>): FreeFiniteModuleElement<R> {
        require(a.module == this)
        return vector { i -> ring.negate(a[i]) }
    }

    override fun subtract(x: FreeFiniteModuleElement<R>, y: FreeFiniteModuleElement<R>): FreeFiniteModuleElement<R> {
        require(x.module == y.module)
        require(x.module == this)
        return vector { i -> ring.subtract(x[i], y[i]) }
    }

    override fun action(r: R, a: FreeFiniteModuleElement<R>): FreeFiniteModuleElement<R> {
        require(a.module == this)
        return vector { i -> ring.multiply(r, a[i]) }
    }


}