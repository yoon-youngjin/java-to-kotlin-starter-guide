package yoon.lec18

import yoon.lec18.Fruit

fun main() {
    val fruits = listOf(
        Fruit(1, "수박", 100, 100),
        Fruit(2, "수박", 100, 300),
        Fruit(3, "수박", 100, 200),

        )

    val apples = fruits.filter { fruit -> fruit.name == "사과" }

    val apples2 = fruits.filterIndexed { idx, fruit ->
        println(idx)
        fruit.name == "사과"
    }

    val applePrices = fruits.filter { fruit -> fruit.name == "사과" }
        .map { fruit -> fruit.currentPrice }
    val applePrices2 = fruits.filter { fruit -> fruit.name == "사과" }
        .mapIndexed { idx, fruit ->
            println(idx)
            fruit.currentPrice
        }

    val applePrices3 = fruits.filter { fruit -> fruit.name == "사과" }
        .mapNotNull { fruit -> fruit.nullOrValue() }

    val isAllApple = fruits.all { fruit -> fruit.name == "사과" }
    val isNoneApple = fruits.none { fruit -> fruit.name == "사과" }
    val isAnyApple = fruits.any { fruit -> fruit.name == "사과" }

    val fruitCount = fruits.count()

    val sortedFruits = fruits.sortedBy { fruit -> fruit.currentPrice }

    sortedFruits.forEach {
        println(it)
    }

    val distinctFruitNames = fruits.distinctBy { fruit -> fruit.name }
        .map { fruit -> fruit.name }

    distinctFruitNames.forEach {
        println(it)
    }

    fruits.first()
    fruits.firstOrNull()


    val map: Map<String, List<Fruit>> = fruits.groupBy { fruit -> fruit.name }

    val map2: Map<Long, Fruit> = fruits.associateBy { fruit -> fruit.id }

    val map3: Map<String, List<Long>> = fruits
        .groupBy({ fruit -> fruit.name }, { fruit -> fruit.factoryPrice })

    val fruitsInList: List<List<Fruit>> = listOf(
        listOf(
            Fruit(1, "수박", 100, 100),
        ),
        listOf(
            Fruit(1, "수박", 100, 100),
        )
    )

    val samePriceFruits = fruitsInList.flatMap { list -> list.samePriceFilter }



}

val List<Fruit>.samePriceFilter: List<Fruit>
    get() = this.filter(Fruit::isSamePrice)

private fun filterFruits(
    fruits: List<Fruit>, filter: (Fruit) -> Boolean
): List<Fruit> {
    return fruits.filter(filter)
}