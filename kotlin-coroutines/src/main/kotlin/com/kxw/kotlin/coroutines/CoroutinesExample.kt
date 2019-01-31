package com.kxw.kotlin.coroutines

import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch

/**
 * http://blog.csdn.net/farmer_cc/article/details/75588119
 * http://kotlinlang.org/docs/reference/coroutines.html
 */

fun main(args: Array<String>) {
    launch(CommonPool) {
        delay(1000L)
        println("World!")
    }
    println("Hello,")
    Thread.sleep(2000L)
}