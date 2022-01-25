package math.abstract_structure.module

import math.abstract_structure.FiniteConcreteCategory
import math.abstract_structure.Ring
import math.martix.AbstractMatrix
import math.martix.identityMatrix

/**
 * Created by CowardlyLion at 2022/1/25 14:18
 */
class FiniteConcreteCategoryOfFiniteFreeModuleWithBaseOverRing<A>(val ring: Ring<A>) : FiniteConcreteCategory<FiniteFreeModuleWithBase<A>, CoordinateWithBase<A>, MatrixArrow<A>>() {

    override val descriptions: MutableSet<String> = ring.descriptions.mapTo(mutableSetOf()) { "category of finite dimensional free module over {$it} with faithful functor to category of small sets." }

    override fun c0(a: CoordinateWithBase<A>): FiniteFreeModuleWithBase<A> = a.moduleSignature

    override val arrows: MutableList<MatrixArrow<A>> = mutableListOf()

    fun registerArrow(sourceBase: String, targetBase: String, matrix: AbstractMatrix<A>) {
        arrows += MatrixArrow(ring.freeModule(sourceBase, matrix.columns), ring.freeModule(targetBase, matrix.rows), matrix)
    }

    override fun id(c0: FiniteFreeModuleWithBase<A>): MatrixArrow<A> = MatrixArrow(c0, c0, ring.identityMatrix(c0.dimension))

    override fun hom(source: FiniteFreeModuleWithBase<A>, target: FiniteFreeModuleWithBase<A>): List<MatrixArrow<A>> {
        return arrows.filter { it.source == source && it.target == target }
    }

}