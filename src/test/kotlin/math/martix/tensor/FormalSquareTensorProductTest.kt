package math.martix.tensor

import kotlinx.coroutines.runBlocking
import math.abstract_structure.instance.ringUInt
import math.martix.formalSquareTensorProduct
import math.martix.matrix
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.random.Random
import kotlin.random.nextUInt

/**
 * Created by CowardlyLion at 2022/1/9 15:25
 */
internal class FormalSquareTensorProductTest {


    @Test
    fun tensorProduct() {
        repeat(100) {
            val i = Random.nextUInt(3u, 4u)
            val j = Random.nextUInt(2u, 3u)
            val k = Random.nextUInt(2u, 3u)
            val l = Random.nextUInt(3u, 4u)
            val m = Random.nextUInt(4u, 5u)
            val mA = ringUInt.matrix(i, i) { _, _ -> Random.nextUInt(10u) }
            val mB = ringUInt.matrix(j, j) { _, _ -> Random.nextUInt(10u) }
            val mC = ringUInt.matrix(k, k) { _, _ -> Random.nextUInt(10u) }
            val mD = ringUInt.matrix(l, l) { _, _ -> Random.nextUInt(10u) }
            val mE = ringUInt.matrix(m, m) { _, _ -> Random.nextUInt(10u) }


            val mAB = ringUInt.formalSquareTensorProduct(mA, mB)
            val mCD = ringUInt.formalSquareTensorProduct(mC, mD)
            val mAB_CD = mAB * mCD
            val mAB_CD1 = runBlocking {
                mAB.timesRowParallel(mCD)
            }
            val mABC = ringUInt.formalSquareTensorProduct(mA, mB, mC)
            val mDE = ringUInt.formalSquareTensorProduct(mD,mE)
            val mABC_DE = mABC * mDE
            val mABC_DE1 = runBlocking {
                mABC.timesRowParallel(mDE)
            }
//            println("$mA\n\n $mB\n\n  $mAB\n\n  $mC\n\n  $mD\n\n $mCD\n\n $mAB_CD \n\n\n")
            println("$mABC \n\n $mDE \n\n $mABC_DE \n\n\n")
            assertEquals(mAB_CD, mAB_CD1)
            assertEquals(mABC_DE, mABC_DE1)
        }
    }
}