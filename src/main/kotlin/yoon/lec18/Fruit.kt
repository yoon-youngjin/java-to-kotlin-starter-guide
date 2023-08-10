package yoon.lec18

data class Fruit(
    val id: Long,
    val name: String,
    val factoryPrice: Long,
    val currentPrice: Long,
) {
    fun nullOrValue(): Fruit? {
        return null
    }
    val isSamePrice: Boolean
        get() = factoryPrice == currentPrice
}
