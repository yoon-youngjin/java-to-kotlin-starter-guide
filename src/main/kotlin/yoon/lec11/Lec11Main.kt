package yoon.lec11

import com.lannstark.lec11.StringUtils

private val t = 3

fun add(a: Int, b: Int): Int {
    println(t)
    return a + b
}

class test internal constructor(
    val price: Int
)

fun main() {
    isDirectoryPath("")
}

class Car(
    internal val name: String,
    private var owner: String,
    _price: Int
) {
    var price = _price
        private set
}
