package math.abstract_structure.module.category

import math.abstract_structure.FiniteConcreteCategory
import math.abstract_structure.Ring
import math.abstract_structure.module.FreeFiniteModuleElement
import math.abstract_structure.module.FreeFiniteModule
import math.abstract_structure.module.freeModule
import math.martix.AbstractMatrix
import math.martix.identityMatrix

/**
 * Created by CowardlyLion at 2022/1/25 14:18
 *
 * generic type in Kotlin is not that convenient as compared to purely functional and dependently typed language like Lean。 (Java拖的后腿233，造出Mod模范畴不是那么方便)
 */
class FiniteConcreteCategoryOfFreeFiniteModule<A>(val ring: Ring<A>) : FiniteConcreteCategory<FreeFiniteModule<A>, FreeFiniteModuleElement<A>, MatrixArrow<A>>() {

    override val descriptions: MutableSet<String> = ring.descriptions.mapTo(mutableSetOf()) { "category of finite dimensional free module over {$it} with faithful functor to category of small sets." }

    override fun c0(a: FreeFiniteModuleElement<A>): FreeFiniteModule<A> = a.module

    override val modules: MutableList<FreeFiniteModule<A>> = mutableListOf()
    override val arrows: MutableList<MatrixArrow<A>> = mutableListOf()

    fun registerBase(module: FreeFiniteModule<A>) {
        modules += module
    }

    fun registerArrow(sourceBase: String, targetBase: String, matrix: AbstractMatrix<A>): MatrixArrow<A> {
        val arrow = MatrixArrow(ring.freeModule(sourceBase, matrix.columns), ring.freeModule(targetBase, matrix.rows), matrix)
        arrows += arrow
        return arrow
    }

    fun registerArrow(sourceModule: FreeFiniteModule<A>, targetModule: FreeFiniteModule<A>, matrix: AbstractMatrix<A>): MatrixArrow<A> {
        require(sourceModule.dimension == matrix.columns)
        require(targetModule.dimension == matrix.rows)
        val arrow = MatrixArrow(sourceModule, targetModule, matrix)
        arrows += arrow
        return arrow
    }

    override fun id(c0: FreeFiniteModule<A>): MatrixArrow<A> = MatrixArrow(c0, c0, ring.identityMatrix(c0.dimension))

    override fun hom(source: FreeFiniteModule<A>, target: FreeFiniteModule<A>): List<MatrixArrow<A>> {
        return arrows.filter { it.source == source && it.target == target }
    }

}