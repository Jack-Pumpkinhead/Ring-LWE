package math.abstract_structure.module

import math.abstract_structure.FiniteConcreteCategory
import math.abstract_structure.Ring
import math.martix.AbstractMatrix
import math.martix.identityMatrix

/**
 * Created by CowardlyLion at 2022/1/25 14:18
 *
 * generic type in Kotlin is not that convenient as compared to purely functional and dependently typed language like Lean。 (Java拖的后腿233，造出Mod模范畴不是那么方便)
 */
class FiniteConcreteCategoryOfFiniteFreeModuleWithBaseOverRing<A>(val ring: Ring<A>) : FiniteConcreteCategory<FiniteFreeModuleWithBase<A>, CoordinateWithBase<A>, MatrixArrow<A>>() {

    override val descriptions: MutableSet<String> = ring.descriptions.mapTo(mutableSetOf()) { "category of finite dimensional free module over {$it} with faithful functor to category of small sets." }

    override fun c0(a: CoordinateWithBase<A>): FiniteFreeModuleWithBase<A> = a.moduleSignature

    override val modules: MutableList<FiniteFreeModuleWithBase<A>> = mutableListOf()
    override val arrows: MutableList<MatrixArrow<A>> = mutableListOf()

    fun registerBase(module: FiniteFreeModuleWithBase<A>) {
        modules += module
    }

    fun registerArrow(sourceBase: String, targetBase: String, matrix: AbstractMatrix<A>): MatrixArrow<A> {
        val arrow = MatrixArrow(ring.freeModule(sourceBase, matrix.columns), ring.freeModule(targetBase, matrix.rows), matrix)
        arrows += arrow
        return arrow
    }

    fun registerArrow(sourceModule: FiniteFreeModuleWithBase<A>, targetModule: FiniteFreeModuleWithBase<A>, matrix: AbstractMatrix<A>): MatrixArrow<A> {
        require(sourceModule.dimension == matrix.columns)
        require(targetModule.dimension == matrix.rows)
        val arrow = MatrixArrow(sourceModule, targetModule, matrix)
        arrows += arrow
        return arrow
    }

    override fun id(c0: FiniteFreeModuleWithBase<A>): MatrixArrow<A> = MatrixArrow(c0, c0, ring.identityMatrix(c0.dimension))

    override fun hom(source: FiniteFreeModuleWithBase<A>, target: FiniteFreeModuleWithBase<A>): List<MatrixArrow<A>> {
        return arrows.filter { it.source == source && it.target == target }
    }

}