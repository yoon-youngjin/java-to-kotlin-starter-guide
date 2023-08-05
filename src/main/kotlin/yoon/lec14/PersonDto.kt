package yoon.lec14

fun main() {
    val dto1 = PersonDto("test", 100)
    val dto2 = PersonDto("test", 100)
    println(dto1 == dto2)
    println(dto1)
    println(dto1.age)

}

data class PersonDto(
    val name: String,
    val age: Int
)
