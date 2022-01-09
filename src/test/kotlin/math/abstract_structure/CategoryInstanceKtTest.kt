package math.abstract_structure

import kotlinx.coroutines.runBlocking
import math.abstract_structure.instance.categoryUIntMatrix
import math.abstract_structure.instance.ringUInt
import math.martix.matrix
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.random.Random
import kotlin.random.nextUInt

/**
 * Created by CowardlyLion at 2022/1/8 13:32
 */
internal class CategoryInstanceKtTest {

    @Test
    fun categoryMatrix() {
        repeat(100) {
            val i = Random.nextUInt(5u, 10u)
            val j = Random.nextUInt(5u, 10u)
            val k = Random.nextUInt(5u, 10u)
            val l = Random.nextUInt(5u, 10u)
            val mA = ringUInt.matrix(i, j) { _, _ -> Random.nextUInt(10u) }
            val mB = ringUInt.matrix(j, k) { _, _ -> Random.nextUInt(10u) }
            val mC = ringUInt.matrix(k, l) { _, _ -> Random.nextUInt(10u) }

            val mAB = mA * mB
            val mAB1 = categoryUIntMatrix.compose(mA, mB)
            val mAB2 = runBlocking { mA.timesRowParallel(mB) }
            val mAB_C = mAB * mC
            val mBC = mB * mC
            val mA_BC = mA * mBC
//            println("$mA \n\n$mB \n\n$mC \n\n$mAB \n\n$mBC \n\n$mAB_C \n\n$mA_BC")
            assertEquals(mAB, mAB1)
            assertEquals(mAB, mAB2)
            assertEquals(mAB_C, mA_BC)
        }


    }
}