package math.abstract_structure.instance

import com.ionspin.kotlin.bignum.integer.BigInteger
import math.abstract_structure.CRing
import math.abstract_structure.Category
import math.martix.AbstractMatrix
import math.martix.identityMatrix

/**
 * Created by CowardlyLion at 2022/1/8 13:14
 */


fun <A> CRing<A>.categoryMatrix(): Category<UInt, AbstractMatrix<A>> = object : Category<UInt, AbstractMatrix<A>> {
    override val descriptions: MutableSet<String> = this@categoryMatrix.descriptions.mapTo(mutableSetOf()) { "category of matrix over ($it)" }
    override fun source(f: AbstractMatrix<A>): UInt = f.rows
    override fun target(f: AbstractMatrix<A>): UInt = f.columns
    override fun composeUnsafe(f: AbstractMatrix<A>, g: AbstractMatrix<A>): AbstractMatrix<A> = f * g
    override fun id(c0: UInt): AbstractMatrix<A> = identityMatrix(c0)
}

val categoryUIntMatrix = ringUInt.categoryMatrix()
fun categoryModularUIntMatrix(modulus: UInt) = ringModularUInt(modulus).categoryMatrix()
val categoryBigIntegerMatrix = ringBigInteger.categoryMatrix()
fun categoryModularBigIntegerMatrix(modulus: BigInteger) = ringModularBigInteger(modulus).categoryMatrix()

//used in common notation (multiply on the left, act on column vector)
fun <A> CRing<A>.categoryMatrixOpposite(): Category<UInt, AbstractMatrix<A>> = object : Category<UInt, AbstractMatrix<A>> {
    override val descriptions: MutableSet<String> = this@categoryMatrixOpposite.descriptions.mapTo(mutableSetOf()) { "opposite category of (category of matrix over ($it))" }
    override fun source(f: AbstractMatrix<A>): UInt = f.columns
    override fun target(f: AbstractMatrix<A>): UInt = f.rows
    override fun composeUnsafe(f: AbstractMatrix<A>, g: AbstractMatrix<A>): AbstractMatrix<A> = g * f
    override fun id(c0: UInt): AbstractMatrix<A> = identityMatrix(c0)
}

val categoryUIntMatrixOpposite = ringUInt.categoryMatrixOpposite()
fun categoryModularUIntMatrixOpposite(modulus: UInt) = ringModularUInt(modulus).categoryMatrixOpposite()
val categoryBigIntegerMatrixOpposite = ringBigInteger.categoryMatrixOpposite()
fun categoryModularBigIntegerMatrixOpposite(modulus: BigInteger) = ringModularBigInteger(modulus).categoryMatrixOpposite()

