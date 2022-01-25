package util.stdlib

import math.enableAssertion

/**
 * Created by CowardlyLion at 2022/1/22 23:35
 */

sealed class AssertionStatus(val lazyMessage: () -> Any)
class AssertionSuccess(lazyMessage: () -> Any = { "Assertion success" }) : AssertionStatus(lazyMessage)
class AssertionFailed(lazyMessage: () -> Any = { "Assertion failed" }) : AssertionStatus(lazyMessage)

val printAssertionSuccessMessage: Boolean = true
val outputStream = System.out

//kotlin have no default lazy assertion yet.
//Platform declaration clash: The following declarations have the same JVM signature
//Overload resolution ambiguity. All these functions match.
inline fun lazyAssert1(block: () -> AssertionStatus) {
    if (enableAssertion) {
        when (val status = block()) {
            is AssertionFailed  -> {
                val message = status.lazyMessage()
                throw AssertionError(message)
            }
            is AssertionSuccess -> {
                if (printAssertionSuccessMessage) {
                    val message = status.lazyMessage()
                    outputStream.println(message)
                }
            }
        }
    }
}

object Assertion {
    fun assert(value: Boolean, lazyMessage: () -> Any = { "Assertion failed" }) {
        if (!value) {
            val message = lazyMessage()
            throw AssertionError(message)
        }
    }
}

inline fun lazyAssert2(block: Assertion.() -> Unit) {
    if (enableAssertion) {
        block(Assertion)
    }
}

inline fun lazyAssert(message: Any = "Assertion failed", block: () -> Boolean) {
    if (enableAssertion) {
        if (!block()) {
            throw AssertionError(message)
        }
    }
}
