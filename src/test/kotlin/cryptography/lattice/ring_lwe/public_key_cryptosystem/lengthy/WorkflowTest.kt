package cryptography.lattice.ring_lwe.public_key_cryptosystem.lengthy

import kotlinx.coroutines.runBlocking
import math.integer.uint.factored.UIntPP
import math.integer.uint.factored.primeFactorization
import math.integer.uint.modular.FieldModularUInt
import math.integer.uint.modular.RingModularUInt
import math.integer.ulong.primeOf
import math.random.randomColumnVector
import org.junit.jupiter.api.Test
import util.stdlib.toString
import kotlin.random.Random

/**
 * Created by CowardlyLion at 2022/2/28 15:07
 */
internal class WorkflowTest {

    @Test
    fun findQ() {
        runBlocking {
            for (i in 1u..20u) {
                val prime = primeOf(i)
                val primeDec = (prime.toUInt() - 1u).primeFactorization()
                println("prime: $prime, primeDec: $primeDec")
            }
        }
    }

    @Test
    fun encryptDecrypt() {
        runBlocking {

            val order = (9u).primeFactorization() as UIntPP
            val workflow = Workflow(Random, order, 10u, 0.01, FieldModularUInt(19u), RingModularUInt(6u), 0.01)
            val dim = workflow.dimension

            var count = 0u
            var success = 0u
            for (i in 0u until 100u) {

                val message = workflow.p.randomColumnVector(dim).columnListAt(0u)
                println("message: $message")
                val encrypt = workflow.encrypt(message)
                println("encrypt: ${encrypt.joinToString("\n") { it.columnListAt(0u).toString() }}")
                val decrypt = workflow.decrypt(encrypt)
                println("decrypt: $decrypt")
                println()
                count++
                if (message == decrypt) {
                    success++
                }
            }
            println("count: $count, success: $success")
        }
    }

    @Test
    fun successRate() {
        runBlocking {

            val order = (9u).primeFactorization() as UIntPP

            var sigma = 0.00
            while (sigma < 1.0) {
                sigma += 0.01

                val workflow = Workflow(Random, order, 10u, sigma, FieldModularUInt(19u), RingModularUInt(6u), sigma)
                val dim = workflow.dimension

                var count = 0u
                var success = 0u
                for (i in 0u until 100u) {
                    val message = workflow.p.randomColumnVector(dim).columnListAt(0u)
                    val encrypt = workflow.encrypt(message)
                    val decrypt = workflow.decrypt(encrypt)
                    count++
                    if (message == decrypt) {
                        success++
                    }
                }
                println("sigma: ${sigma.toString(2u)}, count: $count, success: $success")
            }
        }
    }

}
