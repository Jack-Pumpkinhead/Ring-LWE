package cryptography.lattice.ring_lwe.public_key_cryptosystem.compact

import cryptography.lattice.ring_lwe.public_key_cryptosystem.compact.test.CryptosystemWithTiming
import kotlinx.coroutines.runBlocking
import math.integer.uint.factored.UIntPP
import math.integer.uint.factored.UIntPPPI
import math.integer.uint.factored.primeFactorization
import math.integer.uint.modular.FieldModularUInt
import math.integer.uint.modular.ModularUInt
import math.integer.uint.modular.RingModularUInt
import math.martix.AbstractColumnVector
import math.operation.matrixEquals
import math.random.randomColumnVector
import org.junit.jupiter.api.Test
import util.stdlib.toString
import util.test.findPrime
import kotlin.random.Random
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

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

    fun testBase(order: UIntPPPI, prime: FieldModularUInt, messageBase: RingModularUInt, sigma: Double) {

        val workflow = Cryptosystem(Random, order, prime, messageBase, sigma, sigma, sigma, sigma, sigma)
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


    @OptIn(ExperimentalTime::class)
    fun testBase1(order: UIntPPPI, prime: FieldModularUInt, messageBase: RingModularUInt, sigma: Double) {

        val workflow = CryptosystemWithTiming(Random, order, prime, messageBase, sigma, sigma, sigma, sigma, sigma)
        val dim = workflow.dimension

        val (publicKey, secretKey) = workflow.generateKeys()

        var count = 0u
        var success = 0u
        var totalTime = Duration.ZERO
        var totalTime1 = Duration.ZERO
        for (i in 0u until 100u) {

            val message: AbstractColumnVector<ModularUInt> = workflow.p.randomColumnVector(dim)

            val encrypt: EncryptedMessage
            val time = measureTime {
                encrypt = publicKey.encrypt(message)
            }
            val decrypt: AbstractColumnVector<ModularUInt>
            val time1 = measureTime {
                decrypt = secretKey.decrypt(encrypt)
            }

            count++
            if (matrixEquals(message, decrypt)) {
                success++
            }
            totalTime += time
            totalTime1 += time1
        }
        println("sigma: ${sigma.toString(5u)}, count: $count, success: $success")
        println("encrypt average: ${totalTime / 100}, decrypt average: ${totalTime1 / 100}")
        publicKey.printTimes()
        println()
    }

    //sigma: 0.01, count: 100, success: 100
    //encrypt average: 117.439868ms, decrypt average: 1.053389ms
    //sigma: 0.02, count: 100, success: 100
    //encrypt average: 116.477272ms, decrypt average: 632.388us
    //sigma: 0.03, count: 100, success: 100
    //encrypt average: 116.699047ms, decrypt average: 630.036us
    @Test
    fun successRate() {
        runBlocking {
            val order = (512u).primeFactorization() as UIntPP
            val prime = FieldModularUInt(12289u)

            var sigma = 0.00
            while (sigma < 0.5) {
                sigma += 0.01
                testBase1(order, prime, RingModularUInt(2u), sigma)
            }
        }
    }

    //sigma: 0.05, count: 100, success: 100
    //encrypt average: 6.828452ms, decrypt average: 286.618us
    //sigma: 2.75, count: 100, success: 13
    //encrypt average: 2.957276ms, decrypt average: 11.585us
    @Test
    fun successRate1() {
        runBlocking {

            val order = (18u).primeFactorization()
            val prime = FieldModularUInt(919u)

            var sigma = 0.00
            while (sigma <= 3.5) {
                sigma += 0.05
                testBase1(order, prime, RingModularUInt(2u), sigma)  //lower messageBase increase success rate
            }
        }
    }

    //sigma: 0.03, count: 100, success: 100
    //encrypt average: 85.850735ms, decrypt average: 261.607us
    @Test
    fun successRate2() {
        runBlocking {

            val order = (279u).primeFactorization()
            val prime = FieldModularUInt(1117u)

            var sigma = 0.00
            while (sigma <= 0.2) {
                sigma += 0.01
                testBase1(order, prime, RingModularUInt(2u), sigma)
            }
        }
    }

    @Test
    fun successRate3() {
        runBlocking {

            val order = (600u).primeFactorization()
            val prime = FieldModularUInt(1201u)

            var sigma = 0.00
            while (sigma <= 0.2) {
                sigma += 0.01
                testBase1(order, prime, RingModularUInt(2u), sigma)
            }
        }
    }

    //using fast java Gaussian
    //sigma: 0.01, count: 100, success: 0
    //encrypt average: 178.132635ms, decrypt average: 30.292232ms
    //a: 59.073549ms
    //b1: 29.740711ms
    //b2: 45.391us
    //b3: 35.058us
    //b4: 29.356571ms
    //b5: 63.091us
    //b6: 45.707us
    //b: 26.103us
    //c: 59.685637ms

    //sigma: 0.00001, count: 100, success: 50
    //encrypt average: 171.038521ms, decrypt average: 28.861997ms
    //a: 57.025423ms
    //b1: 28.465539ms
    //b2: 46.134us
    //b3: 35.915us
    //b4: 28.258085ms
    //b5: 56.855us
    //b6: 43.976us
    //b: 33.824us
    //c: 57.011707ms
    @Test
    fun successRate4() {
        runBlocking {

            val order = (2003u).primeFactorization()
            val prime = FieldModularUInt(4007u)

            var sigma = 0.00
            while (sigma <= 0.2) {
                sigma += 0.00001
                testBase1(order, prime, RingModularUInt(2u), sigma)
            }
        }
    }

    //using fast java Gaussian
    //1256210/3900000000 = 0.00032210512
    //sigma: 0.08, count: 100, success: 100
    //encrypt average: 63.873916ms, decrypt average: 4.646823ms
    @Test
    fun successRate5() {
        runBlocking {

            val order = (1138u).primeFactorization()
            val prime = FieldModularUInt(6829u)

            var sigma = 0.00
            while (sigma <= 0.2) {
                sigma += 0.01
                testBase1(order, prime, RingModularUInt(2u), sigma)
            }
        }
    }

    //122684/3900000000 = 0.00003145743
    //using fast java Gaussian
    //sigma: 0.00040, count: 100, success: 100
    //encrypt average: 5.114955ms, decrypt average: 1.442488ms
    //a: 1.618154ms
    //b1: 660.969us
    //b2: 13.277us
    //b3: 8.513us
    //b4: 921.425us
    //b5: 20.755us
    //b6: 23.12us
    //b: 10.759us
    //c: 1.818544ms
    @Test
    fun successRate6() {
        runBlocking {

            val order = (1632u).primeFactorization()
            val prime = FieldModularUInt(6529u)

            var sigma = 0.00
            while (sigma <= 0.2) {
                sigma += 0.0001
                testBase1(order, prime, RingModularUInt(256u), sigma)   //large p (32768) lower success rate (should decrease sigma)
            }
        }
    }

    //using fast java Gaussian
    //sigma: 0.13000, count: 100, success: 88
    //encrypt average: 6.897004ms, decrypt average: 2.863381ms
    //a: 2.234858ms
    //b1: 37.237us
    //b2: 21.974us
    //b3: 17.151us
    //b4: 2.121377ms
    //b5: 47.126us
    //b6: 42.602us
    //b: 12.45us
    //c: 2.356509ms
    @Test
    fun successRate7() {
        runBlocking {

            val order = (2048u).primeFactorization()
            val prime = FieldModularUInt(12289u)

            var sigma = 0.00
            while (sigma <= 0.2) {
                sigma += 0.01
                testBase1(order, prime, RingModularUInt(2u), sigma)
            }
        }
    }

    class SuccessRateUltimateResult(
        val order: UIntPPPI,
        val eulerTotient: UIntPPPI,
        val prime: UInt,
        val total: UInt,
        val success: UInt,
        val encryptTime: Duration,
        val decryptTime: Duration
    )

    @OptIn(ExperimentalTime::class)
    suspend fun successRateUltimateBase(orderRange: UIntRange, messageBase: RingModularUInt, sigma: Double, repeatTimes: UInt): List<SuccessRateUltimateResult> {

        val results = mutableListOf<SuccessRateUltimateResult>()

        for (order in orderRange) {
            val prime = findPrime(order)

            val order1 = order.primeFactorization()
            val workflow = Cryptosystem(Random, order1, FieldModularUInt(prime), messageBase, sigma, sigma, sigma, sigma, sigma)
            val dim = workflow.dimension

            val (publicKey, secretKey) = workflow.generateKeys()

            var success = 0u
            var totalTime = Duration.ZERO
            var totalTime1 = Duration.ZERO
            for (i in 0u until repeatTimes) {

                val message: AbstractColumnVector<ModularUInt> = workflow.p.randomColumnVector(dim)

                val encrypt: EncryptedMessage
                val time = measureTime {
                    encrypt = publicKey.encrypt(message)
                }
                val decrypt: AbstractColumnVector<ModularUInt>
                val time1 = measureTime {
                    decrypt = secretKey.decrypt(encrypt)
                }

                if (matrixEquals(message, decrypt)) {
                    success++
                }
                totalTime += time
                totalTime1 += time1
            }
//            println("order: $order1, prime: $prime")
//            println("sigma: ${sigma.toString(4u)}, count: $count, success: $success")
//            println("encrypt average: ${totalTime / 100}, decrypt average: ${totalTime1 / 100}")
            val eulerTotient = order1.eulerTotient.primeFactorization()

            val encryptAverage = totalTime / 100
            val decryptAverage = totalTime1 / 100
            results += SuccessRateUltimateResult(order1, eulerTotient, prime, repeatTimes, success, totalTime, totalTime1)
            println("$order1 & ${eulerTotient.value} & $prime & $success & ${encryptAverage.toString(DurationUnit.MICROSECONDS, 1)} & ${decryptAverage.toString(DurationUnit.MICROSECONDS, 1)}\\\\")
//            println("div: ${encryptAverage.inWholeNanoseconds/order1.value.toLong()}, ${decryptAverage.inWholeNanoseconds/order1.value.toLong()}")
//            println("div1: ${encryptAverage.inWholeNanoseconds/eulerTotient.value.toLong()}, ${decryptAverage.inWholeNanoseconds/eulerTotient.value.toLong()}")
//            println("\\hline")
        }
        return results
    }

    fun List<SuccessRateUltimateResult>.print() {
        sortedBy { it.eulerTotient.value }.forEach {
            it.apply {
                val encryptAverage = encryptTime / 100
                val decryptAverage = decryptTime / 100
                println("$order & ${eulerTotient.value} & $prime & $success & ${encryptAverage.toString(DurationUnit.MICROSECONDS, 1)} & ${decryptAverage.toString(DurationUnit.MICROSECONDS, 1)}\\\\")
            }
            println("\\hline")
        }
    }

    @Test
    fun successRateUltimate0() = runBlocking {
        val results = successRateUltimateBase(500u..527u, RingModularUInt(2u), 0.01, 100u)
        results.print()
    }

    @Test
    fun successRateUltimate1() = runBlocking {
        val results = successRateUltimateBase(1010u..1037u, RingModularUInt(2u), 0.01, 100u)
        results.print()
    }

    @Test
    fun successRateUltimate01() = runBlocking {
        val results = successRateUltimateBase(500u..527u, RingModularUInt(8u), 0.01, 100u)
        println("-------------------------------------")
        results.print()
    }

    @Test
    fun successRateUltimate11() = runBlocking {
        val results = successRateUltimateBase(1010u..1037u, RingModularUInt(8u), 0.01, 100u)
        println("-------------------------------------")
        results.print()
    }


}