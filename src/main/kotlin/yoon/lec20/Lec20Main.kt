package com.yoon.lec20

fun main() {

    val person = Person("윤영진", 100)

    val value1 = person.let {
        it.age
    }

    val value2 = person.run {
        this.age
    }

    val value3 = person.also {
        it.age
    }

    val value4 = person.apply {
        this.age
    }

    println(value1)
    println(value2)
    println(value3)
    println(value4)

    val value5 = person.let { p ->
        p.age
    }

    val value6 = person.run {
        age
    }

    val strings = listOf("APPLE", "CAR")
    strings.map { it.length }
        .filter { it > 1 }
        .let {
            println(it)
        }

    val numbers = listOf("one", "two", "three", "four")
    val modifiedFirstItem = numbers.first()
        .let { firstItem ->
            if (firstItem.length >= 5) firstItem
            else "!$firstItem"
        }.uppercase()
    println(modifiedFirstItem)

    val list = mutableListOf("one", "two", "three")
        .also { println("four 추가 이전 지금 값: $it") }
        .add("four")

    val numbers2 = mutableListOf("one", "two", "three")
    println("four 추가 이전 지금 값: $numbers2")
    numbers2.add("four")

    println(numbers2)

}

fun printPerson(person: Person?) {
    person?.let {
        println(it.name)
        println(it.age)
    }
}