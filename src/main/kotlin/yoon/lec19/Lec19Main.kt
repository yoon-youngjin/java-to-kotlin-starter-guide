package com.yoon.lec19

class Test(
    val name: String,
    val age: Int,
) {
    operator fun component1(): String {
        return this.name
    }

    operator fun component2(): Int {
        return this.age
    }
}

fun main() {
    val person = Test("유녕진", 100)
    val (name, age) = person
    println(name)
    println(age)

    abc@ for (i in 1..100) {
        for (j in 1..100) {
            if (j == 2) {
                break@abc
            }
        }
    }
}