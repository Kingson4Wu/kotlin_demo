package com.kxw.kotlin.coroutines

import kotlinx.coroutines.*
import java.util.function.Supplier
import java.util.stream.IntStream
import kotlin.system.measureTimeMillis

class CoroutineStream {

    companion object {
        fun <T> execute(inputList: List<Supplier<T>>): List<T>? = runBlocking {


            val deferredList = mutableListOf<Deferred<T>>()
            val resultList = mutableListOf<T>()

            for (i in inputList.indices) {
                deferredList.add(GlobalScope.async { inputList[i].get() })
            }

            for (i in deferredList.indices) {
                resultList.add(deferredList[i].await())
            }

            return@runBlocking resultList
        }
    }

    /*fun <T> execute(inputList: List<Supplier<T>>): List<T>? {
        val deferredList = mutableListOf<Deferred<T>>()
        val resultList = mutableListOf<T>()

        for (i in inputList.indices) {
            deferredList.add(GlobalScope.async { inputList[i].get() })
        }

            for (i in deferredList.indices) {
                resultList.add(deferredList[i].await())
            }
        return resultList
    }*/




}

/*fun main() = runBlocking {
    val elapsedTime = measureTimeMillis {

        val inputList: MutableList<Supplier<String>> = ArrayList()
        IntStream.range(0, 5).forEach { i: Int ->
            inputList.add(Supplier {
                try {
                    Thread.sleep(3000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                i.toString()
            })
        }
        val resultList = execute2(inputList)
        println("resultList size: ${resultList?.size}")
    }

    println("total time: $elapsedTime")
}*/

fun main() {
    val future = GlobalScope.async {
        val inputList: MutableList<Supplier<String>> = ArrayList()
        IntStream.range(0, 5).forEach { i: Int ->
            inputList.add(Supplier {
                try {
                    Thread.sleep(3000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                i.toString()
            })
        }
        return@async execute2(inputList)
    }
    println(future)
}

suspend fun <T> execute2(inputList: List<Supplier<T>>): List<T>? {
    val deferredList = mutableListOf<Deferred<T>>()
    val resultList = mutableListOf<T>()

    for (i in inputList.indices) {
        deferredList.add(GlobalScope.async { inputList[i].get() })
    }

    for (i in deferredList.indices) {
        resultList.add(deferredList[i].await())
    }
    return resultList
}
