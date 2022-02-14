package math.abstract_structure.instance

import com.ionspin.kotlin.bignum.integer.BigInteger
import math.abstract_structure.Ring
import math.abstract_structure.Category
import math.integer.big_integer.RingBigInteger
import math.integer.big_integer.modular.RingModularBigInteger
import math.integer.uint.RingUInt
import math.integer.uint.modular.RingModularUInt
import math.martix.AbstractMatrix
import math.martix.identityMatrix

/**
 * Created by CowardlyLion at 2022/1/8 13:14
 */


fun <A> Ring<A>.categoryMatrix(): Category<UInt, AbstractMatrix<A>> = object : Category<UInt, AbstractMatrix<A>> {
    override val descriptions: MutableSet<String> = this@categoryMatrix.descriptions.mapTo(mutableSetOf()) { "category of matrix over ($it)" }
    override fun source(f: AbstractMatrix<A>): UInt = f.rows
    override fun target(f: AbstractMatrix<A>): UInt = f.columns
    override fun composeUnsafe(f: AbstractMatrix<A>, g: AbstractMatrix<A>): AbstractMatrix<A> = f * g
    override fun id(c0: UInt): AbstractMatrix<A> = identityMatrix(c0)
}

val categoryUIntMatrix = RingUInt.categoryMatrix()
fun categoryModularUIntMatrix(modulus: UInt) = RingModularUInt(modulus).categoryMatrix()
val categoryBigIntegerMatrix = RingBigInteger.categoryMatrix()
fun categoryModularBigIntegerMatrix(modulus: BigInteger) = RingModularBigInteger(modulus).categoryMatrix()

//used in common notation (multiply on the left, act on column vector)
fun <A> Ring<A>.categoryMatrixOpposite(): Category<UInt, AbstractMatrix<A>> = object : Category<UInt, AbstractMatrix<A>> {
    override val descriptions: MutableSet<String> = this@categoryMatrixOpposite.descriptions.mapTo(mutableSetOf()) { "opposite category of (category of matrix over ($it))" }
    override fun source(f: AbstractMatrix<A>): UInt = f.columns
    override fun target(f: AbstractMatrix<A>): UInt = f.rows
    override fun composeUnsafe(f: AbstractMatrix<A>, g: AbstractMatrix<A>): AbstractMatrix<A> = g * f
    override fun id(c0: UInt): AbstractMatrix<A> = identityMatrix(c0)
}

val categoryUIntMatrixOpposite = RingUInt.categoryMatrixOpposite()
fun categoryModularUIntMatrixOpposite(modulus: UInt) = RingModularUInt(modulus).categoryMatrixOpposite()
val categoryBigIntegerMatrixOpposite = RingBigInteger.categoryMatrixOpposite()
fun categoryModularBigIntegerMatrixOpposite(modulus: BigInteger) = RingModularBigInteger(modulus).categoryMatrixOpposite()

