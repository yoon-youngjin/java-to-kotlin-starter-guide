package yoon.lec12

import com.lannstark.lec12.Movable

fun main() {
//    println(Person.newBaby("윤영진"))
//    Singleton.a
    moveSomething(object : Movable {
        override fun move() {
        }

        override fun fly() {
        }

    })
}

private fun moveSomething(movable: Movable) {
    movable.move()
    movable.fly()
}

class Person constructor(
    private var name: String,
    private var age: Int,
) {
    companion object Factory : Log {
        private const val MIN_AGE = 1

        @JvmStatic
        fun newBaby(name: String): Person {
            return Person(name, MIN_AGE)
        }

        override fun log() {
            println("log ...")
        }
    }
}

