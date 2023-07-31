package yoon.lec08


fun main() {
    printAll("A", "B", "C", "D")

    val arr = arrayOf("A", "B", "C")
    printAll(*arr)
}

fun max(a: Int, b: Int) = if (a > b) {
    a
} else {
    b
}

fun repeat(
    str: String,
    num: Int = 3,
    useNewLine: Boolean = true
) {
    for (i in 1..num) {
        if (useNewLine) {
            println(str)
        } else {
            print(str)
        }
    }
}

fun printNameAndGender(name: String, gender: String) {
    println(name)
    println(gender)
}

fun printAll(vararg strings: String) {
    for (str in strings) {
        println(str)
    }
}