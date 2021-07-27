package com.kxw.kotlin.coroutines;

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import java.time.Instant
import java.util.concurrent.TimeUnit
import java.util.stream.IntStream
import kotlin.system.measureTimeMillis

fun main() {

    //concurrent()
    //concurrentSingle()

    //concurrent2()
    //concurrentSingle2()

    //delay不占用线程时间，所以单线程的协程并发也很快？

    //xxx()
    // 100并发 ： 协程用时为 140 ms, 148 ms， 68 ms，192 ms，260 ms
    // 10并发 ： 协程用时为 81 ms; 55 ms;  289 ms
    // 3并发 ： 51 ms; 172 ms; 52 ms

    //xxx2()
    //果然，使用http IO请求后，单线程的线程池，使用协程也慢； 默认的线程池数量套路也是根据cpu核数创建的
    // 100并发 ： 协程用时为 10163 ms ; 9852 ms；10636 ms
    //  10并发 ： 协程用时为 1227 ms；1247 ms；1190 ms

    xxx3()
    // 100并发 ： 线程用时为 2790 ms； 2890 ms；3495 ms
    // 10并发 ：  线程用时为 495 ms； 570 ms；462 ms
    // 3并发 ： 268 ms; 244 ms; 235 ms

    //初步结论：协程并发还是会更快
}


fun concurrent() {
    val scope = CoroutineScope(Dispatchers.Default)//创建协程作用域，Default支持并发
    var count = 0
    repeat(1000) {//重复1000次，每次开启一个协程，count自增1
        scope.launch {
            println("线程id:${Thread.currentThread().id}")
            count++
            println(count)
        }
    }
}

fun concurrentSingle() {
    val scope = CoroutineScope(Dispatchers.Unconfined)//创建协程作用域，使用Unconfined，这样在协程被挂起前都不会改变线程，也就是说协程始终运行在单线程中
    var count = 0
    repeat(1000) {//重复1000次，每次开启一个协程，count自增1
        scope.launch {
            println("线程id:${Thread.currentThread().id}")//这个线程始终不会变，除非你在这里挂起
            count++
            println(count)
        }
    }
}


fun concurrent2() {
    val scope = CoroutineScope(Dispatchers.Default)//创建协程作用域，Default支持并发
    var count = 0

    val useTime = measureTimeMillis {

        repeat(1000) {//重复1000次，每次开启一个协程，count自增1
            scope.launch {
                println("线程id:${Thread.currentThread().id}")
                count++
                println(count)
                delay(100L)
            }
        }

    }
    println("协程用时为 $useTime ms")
}


fun concurrentSingle2() {
    val scope = CoroutineScope(Dispatchers.Unconfined)//创建协程作用域，使用Unconfined，这样在协程被挂起前都不会改变线程，也就是说协程始终运行在单线程中
    var count = 0

    val useTime = measureTimeMillis {

        repeat(1000) {//重复1000次，每次开启一个协程，count自增1
            scope.launch {
                println("线程id:${Thread.currentThread().id}")
                count++
                println(count)
                delay(100L)
            }
        }

    }
    println("协程用时为 $useTime ms")
}

//对比kotlin 协程 和 java线程 并发的效率

fun xxx() {

    val httpClient: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(100, TimeUnit.MILLISECONDS)
        .readTimeout(3000, TimeUnit.MILLISECONDS)
        .writeTimeout(1000, TimeUnit.MILLISECONDS)
        .retryOnConnectionFailure(true)
        .build()

    val scope = CoroutineScope(Dispatchers.Default)//创建协程作用域，Default支持并发
    var count = 0

    println(Instant.now().toEpochMilli())

    val useTime = measureTimeMillis {

        repeat(3) {//重复1000次，每次开启一个协程，count自增1
            scope.launch {
                println("线程id:${Thread.currentThread().id}")
                count++
                println(count)
                val builder: Request.Builder = Request.Builder()
                    .url("http://baidu.com")
                    .get()

                val request: Request = builder.build()

                try {
                    val response = httpClient.newCall(request).execute()

                    if (response.isSuccessful) {
                        // print(response.body?.string())
                    }
                }catch (e:Exception){}
                println(Instant.now().toEpochMilli())

            }
        }

    }
    Thread.sleep(5000)
    println(count)
    println("协程用时为 $useTime ms")
    //协程用时为 140 ms, 148 ms
}

fun xxx2() {

    val httpClient: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(100, TimeUnit.MILLISECONDS)
        .readTimeout(3000, TimeUnit.MILLISECONDS)
        .writeTimeout(1000, TimeUnit.MILLISECONDS)
        .retryOnConnectionFailure(true)
        .build()

    val scope = CoroutineScope(Dispatchers.Unconfined)//创建协程作用域，Default支持并发
    var count = 0

    val useTime = measureTimeMillis {

        repeat(10) {//重复1000次，每次开启一个协程，count自增1
            scope.launch {
                println("线程id:${Thread.currentThread().id}")
                count++
                println(count)
                val builder: Request.Builder = Request.Builder()
                    .url("http://baidu.com")
                    .get()

                val request: Request = builder.build()

                try {
                val response = httpClient.newCall(request).execute()

                if (response.isSuccessful) {
                    // print(response.body?.string())
                }
                }catch (e:Exception){}
            }
        }

    }
    Thread.sleep(10000)
    println(count)
    println("协程用时为 $useTime ms")
    //协程用时为 10163 ms ; 9852 ms
}

//使用java线程池
fun xxx3() {

    val httpClient: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(100, TimeUnit.MILLISECONDS)
        .readTimeout(3000, TimeUnit.MILLISECONDS)
        .writeTimeout(1000, TimeUnit.MILLISECONDS)
        .retryOnConnectionFailure(true)
        .build()

    var count = 0
    println(Instant.now().toEpochMilli())

    val useTime = measureTimeMillis {

        IntStream.range(0, 3).parallel().forEach { i: Int ->
            println("线程id:${Thread.currentThread().id}")
            count++
            println(i)
            val builder: Request.Builder = Request.Builder()
                .url("http://baidu.com")
                .get()

            val request: Request = builder.build()

            try {
                val response = httpClient.newCall(request).execute()

                if (response.isSuccessful) {
                    // print(response.body?.string())
                }
            }catch (e:Exception){}
            println(Instant.now().toEpochMilli())
        }
    }
    Thread.sleep(5000)
    println(count)
    println("线程用时为 $useTime ms")
    //线程用时为 2790 ms
}