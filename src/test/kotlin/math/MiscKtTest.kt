package math

import com.ionspin.kotlin.bignum.integer.toBigInteger
import org.junit.jupiter.api.Test

/**
 * Created by CowardlyLion at 2022/2/9 22:36
 */
internal class MiscKtTest{

    @Test
    fun evenOdd() {
        for (i in 0u..1000u) {
            println("$i, isEven: ${i.toBigInteger().isEven()}, isOdd: ${i.toBigInteger().isOdd()}")
        }
    }

}