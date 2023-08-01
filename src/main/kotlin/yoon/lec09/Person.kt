package yoon.lec09

class Person(
    name: String = "윤영진",
    var age: Int = 27
) {

//    val name = name
//        get() = field.uppercase()

//    val upperCaseName: String
//        get() = this.name.uppercase()

    var name = name
        set(value) {
            field = value.uppercase()
        }

    init {
        if (age <= 0) {
            throw IllegalArgumentException("나이는 ${age}일 수 없습니다.")
        }
        println("초기화 블록")
    }

//    fun isAdult(): Boolean {
//        return this.age >= 20
//    }

//    val isAdult: Boolean
//        get() = this.age >= 20

    constructor(name: String) : this(name, 1) {
        println("부생성자 1")
    }

    constructor() : this("윤영진") {
        println("부생성자 2")
    }
}
