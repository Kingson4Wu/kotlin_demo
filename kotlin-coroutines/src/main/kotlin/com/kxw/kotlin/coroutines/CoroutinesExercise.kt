package com.kxw.kotlin.coroutines

import kotlinx.coroutines.*
import kotlin.concurrent.thread
import kotlin.system.measureTimeMillis

/**
 * https://www.jianshu.com/p/cd2212a85877
 */
fun main() {
    // 创建一条新线程并输出 Hello World.
    thread {
        println("使用线程输出 Hello World! Run in ${Thread.currentThread()}")
    }

    // 创建一个协程并使用协程输出 Hello World.
    GlobalScope.launch {
        println("使用协程输出 Hello World! Run in ${Thread.currentThread()}")
    }

    //创建线程调用的是 #thread 方法, 而创建协程调用的是 GlobalScope#launch 方法


    //demoSleep()

    //demoDelay()

    waitOtherJobThread()

    waitOtherJobCoroutine()


    Thread.sleep(5000L)
}

//暂停线程与暂停协程
fun demoSleep() {
    // 创建并运行一条线程, 在线程中使用 Thread#sleep 暂停线程运行 100ms.
    thread {
        val useTime = measureTimeMillis {
            println("线程启动")
            println("线程 sleep 开始")
            Thread.sleep(100L)
            println("线程结束")
        }
        println("线程用时为 $useTime ms")
    }
}

fun demoDelay() {
    // 创建并运行一条协程, 在协程中使用 #delay 暂停协程运行 100 ms.
    GlobalScope.launch {
        val useTime = measureTimeMillis {
            println("协程启动")
            println("协程 delay 开始")
            delay(100L)
            println("协程结束")
        }
        println("协程用时为 $useTime ms")
    }
}

//等待线程执行结束与等待协程执行结束
/**
 * 线程等待另外一个线程任务完成的方法
 */
private fun waitOtherJobThread() {
    // 启动线程 A
    thread {
        println("线程 A: 启动")

        // 随便定义一个变量用于阻塞线程 A
        val waitThreadB = Object()

        // 启动线程 B
        val threadB = thread {
            println("线程 B: 启动")
            println("线程 B: 开始执行任务")
            for (i in 0..99) {
                Math.E * Math.PI
            }
            println("线程 B: 结束")
        }

        // 线程 A 等待线程 B 完成任务
        println("线程 A: 等待线程 B 完成")
        threadB.join()
        println("线程 A: 等待结束")

        println("线程 A: 结束")
    }
}

/**
 * 协程等待另外一个协程任务完成的方法
 */
private fun waitOtherJobCoroutine() {
    // 启动协程 A
    GlobalScope.launch {
        println("协程 A: 启动")

        // 启动协程 B
        val coroutineB = GlobalScope.launch {
            println("协程 B: 启动")
            println("协程 B: 开始执行任务")
            for (i in 0..99) {
                Math.E * Math.PI
            }
            println("协程 B: 结束")
        }

        // 协程 A 等待协程 B 完成
        println("协程 A: 等待协程 B 完成")
        coroutineB.join()
        println("协程 A: 等待结束")

        println("协程 A: 结束")
    }
}

//中断线程与中断协程
private fun cancelThread() {
    val job1 = thread {
        println("线程: 启动")

        // 循环执行 100 个耗时任务.
        for (i in 0..99) {
            try {
                Thread.sleep(50L)
                println("线程: 正在执行任务 $i...")
            } catch (e: InterruptedException) {
                println("线程: 被中断了")
                break
            }
        }

        println("线程: 结束")
    }

    // 延时 200ms 后中断线程.
    Thread.sleep(200L)
    println("中断线程!!!")
    job1.interrupt()
}

private fun cancelCoroutine() = runBlocking {
    val job1 = GlobalScope.launch {
        println("协程: 启动")

        // 循环执行 100 个耗时任务.
        for (i in 0..99) {
            try {
                delay(50L)
                println("协程: 正在执行任务 $i...")
            } catch (cancelException: CancellationException) {
                println("协程: 被中断了")
                break
            }
        }

        println("协程: 结束")
    }

    // 延时 200ms 后中断协程.
    delay(200L)
    println("中断协程!!!")
    job1.cancel()
}
//runBlocking 文档是如何描述该函数的：
//Runs a new coroutine and blocks the current thread interruptibly until its completion. This function should not be used from a coroutine. It is designed to bridge regular blocking code to libraries that are written in suspending style, to be used in main functions and in tests.
//运行一个新的协程并且阻塞当前可中断的线程直至协程执行完成，该函数不应从一个协程中使用，该函数被设计用于桥接普通阻塞代码到以挂起风格（suspending style）编写的库，以用于主函数与测试。

//这要从 suspend 修饰符说起，协程使用中可以使用该修饰符修饰一个函数，表示该函数为挂起函数，从而运行在协程中。 挂起函数，它不会造成线程阻塞，但是会 挂起 协程，并且只能在协程中使用。挂起函数不可以在main函数中被调用，那么我们怎么调试呢？对了，就是使用runBlocking 函数！
//————————————————
//版权声明：本文为CSDN博主「Junerver」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
//原文链接：https://blog.csdn.net/u011133887/article/details/98617852

//总结：runBlocking 方法，可以在普通的阻塞线程中开启一个新的协程以用于运行挂起函数，并且可以在协程中通过调用 launch 方法，开启一个子协程，用于运行后台阻塞任务。