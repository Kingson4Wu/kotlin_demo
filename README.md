Kotlin_demo

+ Kotlin 1.2 的新增特性:<https://www.oschina.net/translate/whats-new-in-kotlin-12>

+ 6个能让你的 Kotlin 代码库更有意思的“魔法糖”:<https://www.oschina.net/translate/6-magic-sugars-make-your-kotlin-codebase-happier-part-1>

<pre>
import java.util.*

data class TestDataBean(
        val title: String = "",
        val count: Int = 0,
        val createTime: Date = Date()
)

fun main(args: Array<String>) {
    val dataBean1 = TestDataBean("Test Title", 1, Date())
    val dataBean2 = TestDataBean(title = "Test Title")
    val dataBean3 = TestDataBean()
    val (title, count) = dataBean3
    println("$title $count")
}
</pre>

