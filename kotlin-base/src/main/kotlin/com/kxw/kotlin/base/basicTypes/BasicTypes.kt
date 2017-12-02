package com.kxw.kotlin.base.basicTypes


/**
 * Kotlin 基本数据类型
 *  Byte、Short、Int、Long、Float、Double
 *
 */

/**
 * Kotlin 中没有基础数据类型，只有封装的数字类型，你每定义的一个变量，其实 Kotlin 帮你封装了一个对象，这样可以保证不会出现空指针。
 * 在 Kotlin 中，三个等号 === 表示比较对象地址，两个 == 表示比较两个值大小。
 */

fun main(args: Array<String>) {
    /**
     * 这里我把 a 的值换成 100，这里应该跟 Java 中是一样的，在范围是 [-128, 127] 之间并不会创建新的对象，比较输出的都是 true，从 128 开始，比较的结果才为 false。
     */
    val a: Int = 100
    println(a === a) // true，值相等，对象地址相等

    //经过了装箱，创建了两个不同的对象
    val boxedA: Int? = a
    val anotherBoxedA: Int? = a

    //虽然经过了装箱，但是值是相等的，都是10000
    println(boxedA === anotherBoxedA) //  false，值相等，对象地址不一样
    println(boxedA == anotherBoxedA) // true，值相等
}