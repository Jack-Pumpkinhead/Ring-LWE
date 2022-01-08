package math.abstract_structure

import math.martix.Matrix
import math.martix.identityMatrix

/**
 * Created by CowardlyLion at 2022/1/8 13:14
 */


fun <A> Ring<A>.categoryMatrix(): Category<UInt, Matrix<A>> = object : Category<UInt, Matrix<A>>() {
    override fun source(f: Matrix<A>): UInt = f.rows
    override fun target(f: Matrix<A>): UInt = f.columns
    override fun compositeImpl(f: Matrix<A>, g: Matrix<A>): Matrix<A> = f * g
    override fun id(c0: UInt): Matrix<A> = identityMatrix(c0)
}