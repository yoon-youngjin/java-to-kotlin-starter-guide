package yoon.lec16

import com.lannstark.lec16.Person
import java.lang.IllegalArgumentException

fun main() {
    val str = "ABC"
    println(str.lastChar())

    val person = Person("윤", "영진", 100)
    println(person.nextYearAge())

    val train: Train = Train()
    train.isExpensive() // Train의 확장 함수

    val srt1: Train = Srt()
    srt1.isExpensive() // Train의 확장 함수

    val srt2: Srt = Srt()
    srt2.isExpensive() // Srt의 확장 함수

    println(3.add(4))
    println(3.add2(4))
    println(3 add2 4)

    println(3.add3(4))

}

fun String.lastChar(): Char {
    return this[this.length - 1]
}

val String.lastChar: Char
    get() = this[this.length - 1]

fun Person.nextYearAge(): Int {
    println("확장 함수")
    return this.age + 1
}

open class Train(
    val name: String = "새마을기차",
    val price: Int = 5000,
)

fun Train.isExpensive(): Boolean {
    println("Train의 확장 함수")
    return this.price >= 10000
}

class Srt : Train("SRT", 40000)

fun Srt.isExpensive(): Boolean {
    println("Srt의 확장 함수")
    return this.price >= 10000
}

fun Int.add(other: Int): Int {
    return this + other
}

infix fun Int.add2(other: Int): Int {
    return this + other
}

inline fun Int.add3(other: Int): Int {
    return this + other
}

fun createPerson(firstName: String, lastName: String): Person {
    fun validateName(name: String, fieldName: String) {
        if (name.isEmpty()) {
            throw IllegalArgumentException("${fieldName}은 비어있을 수 없습니다! 현재 값 : $name")
        }
    }
    validateName(firstName, "firstName")
    validateName(lastName, "lastName")

    return Person(firstName, lastName, 1)
}