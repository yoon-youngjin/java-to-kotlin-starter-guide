package yoon.lec04

import com.lannstark.lec04.JavaMoney


fun main() {
    val money1 = JavaMoney(2_000L)
    val money2 = money1
    val money3 = JavaMoney(2_000L)

    println(money1 === money2)
    println(money1 === money3)
    println(money1 == money3)

    if (money1 > money2) {
        println("Money1이 Money2보다 금액이 큽니다.")
    }

    if (fun1() || fun2()) { }


}

fun fun1(): Boolean {
    println("fun1")
    return true
}

fun fun2(): Boolean {
    println("fun2")
    return false;
}

