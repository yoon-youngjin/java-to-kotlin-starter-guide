package yoon.lec15

fun main() {

    /*val arr = arrayOf(100, 200)

    for (i in arr.indices) {
        println("${i} ${arr[i]}")
    }

    for ((idx, value) in arr.withIndex()) {
        println("$idx $value")
    }

    arr.plus(300)

    val numbers = listOf(100, 200)
    val emptyList = emptyList<Int>()
    printNumbers(emptyList())

    println(numbers[0])
    println(numbers.get(0))

    for (number in numbers) {
        println(number)
    }

    for ((idx, value) in numbers.withIndex()) {
        println("$idx $value")
    }

    val nums = mutableListOf(100, 200)
    nums.add(300)*/

    val numberSet = setOf(100, 200, 300)

    // For Each
    for (number in numberSet) {
        println(number)
    }

    // 전통적인 For문
    for ((idx, number) in numberSet.withIndex()) {
        println("$idx $number")
    }

    val oldMap = mutableMapOf<Int, String>()
    oldMap.put(1, "MONDAY")
    oldMap[1] = "MONDAY"
    oldMap[2] = "TUESDAY"

    mapOf(1 to "MONDAY", 2 to "TUESDAY")

    for (key in oldMap.keys) {
        println(key)
        println(oldMap[key])
    }

    for ((key, value) in oldMap.entries) {
        println(key)
        println(value)
    }

}

private fun printNumbers(numbers: List<Int>) {

}