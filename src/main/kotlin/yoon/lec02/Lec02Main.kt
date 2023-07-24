package yoon.lec02

import com.lannstark.lec02.Person

fun main() {
//    val str: String? = null
//    str.length
//    println(str?.length ?: 0)
//    str?.length
//    println(startsWith(str))

    val person = Person("윤영진")
    println(startsWith(person.name))
}
fun startsWith(str: String): Boolean {
    return str.startsWith("A")
}

//fun startsWithA(str: String?): Boolean {
//    return str.startsWith("A")
//}

fun startsWithA1(str: String?): Boolean {
    return str?.startsWith("A")
        ?: throw IllegalArgumentException("null이 들어왔습니다")
}

fun startsWithA2(str: String?): Boolean? {
    return str?.startsWith("A")
}

fun startsWithA3(str: String?): Boolean {
    return str?.startsWith("A")
        ?: false
}

//fun startsWith(str: String?): Boolean {
//    return str!!.startsWith("A")
//}

