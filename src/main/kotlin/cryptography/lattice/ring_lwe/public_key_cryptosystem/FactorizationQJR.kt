package cryptography.lattice.ring_lwe.public_key_cryptosystem

import math.martix.AbstractDiagonalMatrix
import math.martix.AbstractSquareMatrix

/**
 * Created by CowardlyLion at 2022/2/26 18:43
 *
 * square matrix factorization of M = QJR with Q unitary, J positive diagonal, R upper triangular with diagonal 1.
 *
 * q is not used in Ring-LWE crypto-system
 */
class FactorizationQJR<A>(val q: AbstractSquareMatrix<A>?, val j: AbstractDiagonalMatrix<A>, val r: AbstractSquareMatrix<A>) {

    init {
        require(j.size == r.size)
    }

}