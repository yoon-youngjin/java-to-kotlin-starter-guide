package yoon.lec09

import com.lannstark.lec09.JavaPerson

fun main() {
    val person = Person("윤영진", 27)
    println(person.name)
    person.age = 28

    val javaPerson = JavaPerson("윤영진", 27)
    println(javaPerson.name)
    javaPerson.age = 28

    val person2 = Person("윤영진")

    val person3 = Person()

}