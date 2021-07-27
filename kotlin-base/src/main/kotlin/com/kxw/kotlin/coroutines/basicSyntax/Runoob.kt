package com.kxw.kotlin.coroutines.basicSyntax

/**
 * http://www.runoob.com/kotlin/kotlin-basic-syntax.html
 *
 * kotlin源文件不需要相匹配的目录和包，源文件可以放在任何文件目录。
 * 例中 test() 的全名是 com.runoob.main.test、Runoob 的全名是 com.runoob.main.Runoob。
 * 如果没有指定包，默认为 default 包。
 */
fun test() {}

class Runoob {}

fun sum(a: Int, b: Int): Int {   // Int 参数，返回值 Int
    return a + b
}


/** 可变长参数函数 */
fun vars(vararg v: Int) {
    for (vt in v) {
        print(vt)
    }
}

// 测试
/*fun main(args: Array<String>) {
    vars(1,2,3,4,5)  // 输出12345
}*/

/** 字符串模板 */
var a = 1
// 模板中的简单名称：
val s1 = "a is $a"

/*fun main(args: Array<String>) {

    a = 2
    // 模板中的任意表达式：
    val s2 = "${s1.replace("is", "was")}, but now is $a"

    print(s2)
}*/

/** NULL检查机制 */
//类型后面加?表示可为空
var age: String? = "23"
//抛出空指针异常
val ages = age!!.toInt()
//不做处理返回 null
val ages1 = age?.toInt()
//age为空返回-1
val ages2 = age?.toInt() ?: -1

// 当一个引用可能为 null 值时, 对应的类型声明必须明确地标记为可为 null。

//类型检测及自动类型转换

fun main() {

    // 使用 step 指定步长
    for (i in 1..4 step 2) print(i) // 输出“13”

    for (i in 4 downTo 1 step 2) print(i) // 输出“42”


// 使用 until 函数排除结束元素
    for (i in 1 until 10) {   // i in [1, 10) 排除了 10
        println(i)
    }
}




