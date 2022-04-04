package cryptography.lattice.ring_lwe.public_key_cryptosystem.compact

import kotlinx.coroutines.runBlocking
import math.integer.uint.factored.UIntPP
import math.integer.uint.factored.UIntPPPI
import math.integer.uint.factored.primeFactorization
import math.integer.uint.modular.FieldModularUInt
import math.integer.uint.modular.RingModularUInt
import math.operation.matrixEquals
import math.random.randomColumnVector
import org.junit.jupiter.api.Test
import util.stdlib.toString
import kotlin.random.Random

/**
 * Created by CowardlyLion at 2022/4/5 0:08
 */
internal class CryptosystemTest {

    @Test
    fun encryptDecrypt() {
        runBlocking {
            val error = 0.01

            val order = (9u).primeFactorization() as UIntPP
            val prime = FieldModularUInt(19u)
            val workflow = Cryptosystem(Random, order, prime, RingModularUInt(6u), error, error, error, error, error)
            val dim = workflow.dimension

            val (publicKey, secretKey) = workflow.generateKeys()

            var count = 0u
            var success = 0u
            for (i in 0u until 100u) {

                val message = workflow.p.randomColumnVector(dim)
                println("message: ${message.columnListAt(0u)}")
                val encrypt = publicKey.encrypt(message)
                println("encrypt: $encrypt")
                val decrypt = secretKey.decrypt(encrypt)
                println("decrypt: ${decrypt.columnListAt(0u)}")
                println()
                count++
                if (matrixEquals(message, decrypt)) {
                    success++
                }
            }
            println("count: $count, success: $success")
        }
    }

    fun testBase(order: UIntPPPI, prime: FieldModularUInt, sigma: Double) {

        val workflow = Cryptosystem(Random, order, prime, RingModularUInt(6u), sigma, sigma, sigma, sigma, sigma)
        val dim = workflow.dimension

        val (publicKey, secretKey) = workflow.generateKeys()

        var count = 0u
        var success = 0u
        for (i in 0u until 100u) {
            val message = workflow.p.randomColumnVector(dim)
            val encrypt = publicKey.encrypt(message)
            val decrypt = secretKey.decrypt(encrypt)
            count++
            if (matrixEquals(message, decrypt)) {
                success++
            }
        }
        println("sigma: ${sigma.toString(2u)}, count: $count, success: $success")
    }

    @Test
    fun successRate() {
        runBlocking {

            val order = (512u).primeFactorization() as UIntPP
            val prime = FieldModularUInt(12289u)

            var sigma = 0.00
            while (sigma < 0.5) {
                sigma += 0.01
                testBase(order, prime, sigma)
            }
        }
    }

    @Test
    fun successRate1() {
        runBlocking {

            val order = (18u).primeFactorization()
            val prime = FieldModularUInt(919u)

            var sigma = 0.00
            while (sigma < 5.0) {
                sigma += 0.05
                testBase(order, prime, sigma)
            }
        }
    }


}