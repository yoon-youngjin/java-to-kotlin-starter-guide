## 18강. 코틀린에서 컬렉션을 함수형으로 다루는 방법

### 목차

1. [필터와 맵](#1-필터와-맵)
2. [다양한 컬렉션 처리 기능](#2-다양한-컬렉션-처리-기능)
3. [List를 Map으로](#3-list를-map으로)
4. [중첩된 컬렉션 처리](#4-중첩된-컬렉션-처리)

### 1. 필터와 맵

```kotlin
data class Fruit(
    val id:Long,
    val name: String,
    val factoryPrice: Long,
    val currentPrice: Long,
)
```

위와 같은 Fruit 데이터가 존재하는 경우, 사과만 달라는 요구사항 혹은 사과의 가격을 알려달라는 요구사항이 발생한다면?

**filter : 사과만 주세요**

```kotlin
val apples = fruits.filter { fruit -> fruit.name == "사과" }
```

필터에서 인덱스가 필요하다면?

```kotlin
val apples = fruits.filterIndexed { idx, fruit ->
        println(idx)
        fruit.name == "사과"
}
```

**map :사과의 가격들을 알려주세요**

```kotlin
val applePrices = fruits.filter { fruit -> fruit.name == "사과" }
        .map { fruit -> fruit.currentPrice }
```

맵에서 인덱스가 필요하다면?

```kotlin
val applePrices = fruits.filter { fruit -> fruit.name == "사과" }
        .mapIndexed { idx, fruit ->
            println(idx)
            fruit.currentPrice
        }
```

Mapping 결과가 `null`이 아닌 것만 가져오고 싶다면?

```kotlin
val applePrices3 = fruits.filter { fruit -> fruit.name == "사과" }
        .mapNotNull { fruit -> fruit.nullOrValue() }
```
- 예를 들어, `nullOrValue()`의 결과가 `null`일 가능성이 존재하는데, `null`이 아닌 것만을 고르고 싶은 경우

이전 코드를 보면

```kotlin
private fun filterFruits(
    fruits: List<Fruit>, filter: (Fruit) -> Boolean
): List<Fruit> {

    val results = mutableListOf<Fruit>()
    for (fruit in fruits) {
        if (filter(fruit)) {
            results.add(fruit)
        }
    }

    return results
}
```

위 코드를 바꿔보면

```kotlin
private fun filterFruits(
    fruits: List<Fruit>, filter: (Fruit) -> Boolean
): List<Fruit> {
    return fruits.filter(filter)
}
```

### 2. 다양한 컬렉션 처리 기능

"모든 과일이 사과인가요?" 라는 질문 혹은 "출고가 10,000원 이상의 과일이 하나라도 있나요?" 라는 질문을 처리하는 기능으로

**all : 조건을 모두 만족하면 true 그렇지 않으면 false**

```kotlin
val isAllApple = fruits.all { fruit -> fruit.name == "사과" }
```

**none : 조건을 모두 불만족하면 true 그렇지 않으면 false**

```kotlin
val isNoneApple = fruits.none { fruit -> fruit.name == "사과" }
```

**any : 조건을 하나라도 만족하면 true 그렇지 않으면 false**

```kotlin
val isNoApple = fruits.any { fruit -> fruit.name == "사과" }
```

"총 과일 개수가 몇개인가요?" 라는 질문 혹은 "낮은 가격 순으로 보여주세요" 라는 요구사항, "과일이 몇 종류가 있나요?" 라는 질문의 경우에

**count : 개수를 센다**

```kotlin
val fruitCount = fruits.count()
```

**sortedBy : (오름차순) 정렬을 한다.**

```kotlin
val sortedFruits = fruits.sortedBy { fruit -> fruit.currentPrice }
```
- 현재 가격순으로 오름차순 정렬

**sortedByDescending : (내림차순) 정렬을 한다.**

```kotlin
val sortedFruits = fruits.sortedByDescending { fruit -> fruit.currentPrice }
```

**distinctBy : 변형된 값을 기준으로 중복을 제거한다**

```kotlin
val distinctFruitNames = fruits.distinctBy { fruit -> fruit.name}
    .map { fruit -> fruit.name }
```
- `fruit`의 `name`을 기준으로 중복을 제거하므로 여러 개의 사과가 존재하더라 1개만 남게 된다.


"첫번째 과일만 주세요" 혹은 "마지막 과일만 주세요"라는 요구사항의 경우에

**first : 첫번째 값을 가져온다 (무조건 `null`이 아니어야함)**

**firstOrNull : 첫번째 값 또는 `null`을 가져온다**

**last : 마지막 값을 가져온다 (무조건 `null`이 아니어야함)**

**lastOrNull : 마지막 값 또는 `null`을 가져온다**

`first`, `last`의 경우 `null`이라면 Exception이 발생한다. 

### 3. List를 Map으로

과일 이름이 Key이고 과일 이름을 토대로 과일들이 존재하는 map을 만들어야 하는 경우에

```kotlin
val map: Map<String, List<Fruit>> = fruits.groupBy { fruit -> fruit.name }
```

id가 Key이고 value가 과일인 map이 필요한 경우에

```kotlin
val map: Map<String, Fruit> = fruits.associateBy { fruit -> fruit.id }
```
- 중복되지 않은 Key를 가지고 map 을 만들고 싶은 경우 사용할 수 있다.

Key와 value를 동시에 처리할 수도 있다.

```kotlin
val map: Map<String, List<Long>> = fruits
        .groupBy({fruit -> fruit.name}, {fruit -> fruit.factoryPrice })
```
- 위와 같이 작성하면 과일 이름을 Key로 value에는 출고가 리스트가 들어가는 map을 만들 수 있다.

id -> 출고가 Map이 필요한 경우에는

```kotlin
val map: Map<Long, Long> = fruits
        .associateBy({fruit -> fruit.id}, {fruit -> fruit.factoryPrice })
```

추가적으로 Map에 대해서도 앞선 기능들을 대부분 사용할 수 있다.

```kotlin
val map: Map<String, List<Fruit>> = fruits.groupBy{ fruit -> fruit.name }
    .filter { (key, value) -> key == "사과"}
```

### 4. 중첩된 컬렉션 처리

```kotlin
val fruitsInList: List<List<Fruit>> = listOf(
    listOf(
        ...
    ),
    listOf(
        ...
    ),
    ...
)
```

위와 같이 리스트 안에 리스트가 들어가 있는 구조에서 출고가와 현재가가 동일한 과일을 골라달라는 요구사항이 존재한다면

```kotlin
val samePriceFruits = fruitsInList.flatMap { list -> 
    list.filter { fruit -> fruit.factoryPrice == fruit.currentPrice }
}
```
- `flatMap`을 사용하면 `List<List<타입>>`이 `List<타입>`으로 변경된다.

또한 다음과 같이 리팩토링이 가능해진다.

```kotlin
val List<Fruit>.samePriceFilter: List<Fruit>
    get() = this.filter(Fruit::isSamePrice)

data class Fruit(
    val id: Long,
    val name: String,
    val factoryPrice: Long,
    val currentPrice: Long,
) {
    
    val isSamePrice: Boolean
        get() = factoryPrice == currentPrice
}

val samePriceFruits = fruitsInList.flatMap { list -> list.samePriceFilter }
```

`List<List<Fruit>>`를 `List<Fruit>`으로 바꾸는 경우에는

```kotlin
fruitsInList.flatten()
```