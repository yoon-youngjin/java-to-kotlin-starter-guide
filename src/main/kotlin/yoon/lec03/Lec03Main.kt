package yoon.lec03

import com.lannstark.lec03.Person

fun main() {
    val num1 = 4
    val num2: Long = num1.toLong()
    println(num1 + num2)

    val num3 = 3
    val num4 = 5
    val result = num3 / num4.toDouble()
    println(result)

    val num5: Int? = 3
    val num6: Long = num5?.toLong() ?: 0L

    printAgeIfPerson(null)

    val person = Person("윤영지", 27)
    println("이름: ${person.name}, 나이: ${person.age}")

    val withoutIndent =
        """
            ABC
            123
            456
    """.trimIndent()
    println(withoutIndent)

    val str = "ABCDE"
    println(str[1])
    println(str[2])

}

fun printAgeIfPerson(obj: Any?) {
    val person = obj as? Person
    println(person?.age)
}