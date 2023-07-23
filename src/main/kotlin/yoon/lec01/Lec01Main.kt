package yoon.lec01

import com.lannstark.lec01.Person

fun main() {
    var number1 = 10L
    val number2 = 10L

    var number3: Long
    number3 = 5L
    println(number3)

    val number4: Long
    number4 = 10L
    println(number4)

    val numbers: List<Int> = listOf(1, 2)

    var number5: Long? = 10L
    number5 = null

    var person = Person("윤영진")

}