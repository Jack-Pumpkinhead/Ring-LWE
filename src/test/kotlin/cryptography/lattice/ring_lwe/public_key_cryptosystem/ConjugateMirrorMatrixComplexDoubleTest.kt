package cryptography.lattice.ring_lwe.public_key_cryptosystem

import math.operation.matrixToStringComplexDouble
import org.junit.jupiter.api.Test

/**
 * Created by CowardlyLion at 2022/2/24 22:04
 */
internal class ConjugateMirrorMatrixComplexDoubleTest{

    @Test
    fun print() {
        for (i in 1u..10u) {
            val matrix = ConjugateMirrorMatrixComplexDouble(2u * i, i)
            println(matrixToStringComplexDouble(matrix))
        }
    }
}