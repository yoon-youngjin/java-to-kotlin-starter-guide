## 19강. 코틀린의 이모저모

### 목차

1. [Type Alias 와 as import](#1-type-alias-와-as-import)
2. [구조분해와 componentN 함수](#2-구조분해와-componentn-함수)
3. [Jump와 Label](#3-jump와-label)
4. [TakeIf와 TakeUnless](#4-takeif와-takeunless)

### 1. Type Alias 와 as import

#### Type Alias

긴 이름의 클래스 혹은 함수 타입이 있을 때, 축약하거나 더 좋은 이름을 쓰고 싶을 경우 사용할 수 있다.

```kotlin
fun filterFruits(fruits: List<Fruit>, filter: (Fruit) -> Boolean) {
    
}
```

위와 같이 `filter`라는 함수를 파라미터로 받고 있는 상황에서 `(Fruit) -> Boolean` 이란 타입이 너무 길다고 생각되고, 파라미터가 많아지는 경우를 생각해볼 수 있다.
이런 경우에 타입을 축약하고 싶을 수 있다. 

```kotlin
typealias FruitFilter = (Fruit) -> Boolean

fun filterFruits(fruits: List<Fruit>, filter: FruitFilter) {

} 
```

```kotlin
data class UltraSuperGuardianTribe(
    val name: String
)

typealias USGTMap = Map<String, UltraSuperGuardianTribe>
```

이름 긴 클래스를 컬렉션에 사용할 때도 간단히 줄일 수 있다. 

#### as import

다른 패키지의 같은 이름 함수를 동시에 가져오고 싶은 경우 사용할 수 있다.

```kotlin
package com.yoon.lec19.a

fun printHelloWorld() {
    println("Hello World A")
}
```
```kotlin
package com.yoon.lec19.b

fun printHelloWorld() {
    println("Hello World B")
}
```

이름이 같은 서로 다른 함수를 가져오기 위해서는 as import를 사용해야 한다.

> as import : 어떤 클래스나 함수를 import 할 때 이름을 바꾸는 기능

```kotlin
import com.yoon.lec19.a.printHelloWorld as printHelloWorldA
import com.yoon.lec19.b.printHelloWorld as printHelloWorldB

fun main() {
    printHelloWorldA()
    printHelloWorldB()
}
```

### 2. 구조분해와 componentN 함수

> 구조분해 : 복합적인 값을 분해하여 여러 변수를 한 번에 초기화하는 것

```kotlin
val person = Person("윤영진", 50)
val (name, age) = person
```

Data Class는 componentN이란 함수도 자동으로 만들어준다.

`val (name, age) = person`이라는 한 줄은 아래 두 줄을 합친 개념이다.

```kotlin
val name = person.component1()
val age = person.component2()
```
- 데이터 클래스는 기본적으로 자신이 가지고 있는 field에 대해서 componentN 함수를 만들어 준다.
  - 즉, `component1()`은 첫 번째 프로퍼티를 가져오는 것이다.
  - 구조분해 문법을 쓴다는 것은 componentN 함수를 호출한다는 뜻이다.
  - 순서가 잘못되면 잘못된 결과가 나온다. 단순히 값을 가져와 대입해주는 것이기 때문에

**Data Class가 아닌데 구조분해를 사용하고 싶다면, componentN 함수를 직접 구현할 수 있다.**

```kotlin
class Test(
    val name: String,
    val age: Int,
) {
    operator fun component1(): String {
        return this.name
    }

    operator fun component2(): Int {
        return this.age
    }
}

fun main() {
    val test = Test("유녕진", 100)
    val (name, age) = test
}
```
- 주의할 점으로는 componentN 함수는 연산자의 속성을 가지고 있기 때문에 연산자 오버로딩으로 간주되어야 하므로 `operator` 키워드를 붙어야 한다.

```kotlin
val map = mapOf(1 to "A", 2 to "B")
for ((key, value) in map.entries) {
    
}
```

위 문법 역시 구조분해다. 

### 3. Jump와 Label

기본적으로 코드의 흐름을 제어할 때 return, break, continue 키워드가 있다.
위 키워드를 코틀린에서는 아래와 같이 정의하고 있다.
- return : 기본적으로 가장 가까운 enclosing function 또는 익명함수로 값이 반환된다.
- break : 가장 가까운 루프가 제거된다.
- continue : 가장 가까운 루프를 다음 step으로 보낸다.

따라서, for문 및 while 문에서 break, continue 기능은 동일하다. 단,

```kotlin
val numbers = listOf(1, 2, 3)
numbers.map { number -> number + 1}
    .forEach { number -> println(number) }
```

위와 같이 함수형 프로그래밍에서 무언가를 반복되게 쓰고 싶을 때 forEach 기능을 사용할 수 있는데,
forEach에서 주의해야 할 것은 continue, break를 사용할 수 없다. 

forEach문과 함께 break 또는 continue를 쓰고 싶다면 

**break**

```kotlin
val numbers = listOf(1, 2, 3)
run {
    numbers.forEach { number ->
        if (number == 3) {
            return@run
        }
      println(number)
    }
}
```

break는 run이라는 블록으로 감싼 뒤 `return@run`을 작성한다.

**continue**

```kotlin
val numbers = listOf(1, 2, 3)
numbers.forEach { number ->
    println(number)
    if (number == 3) {
        return@forEach
    }
}
```

`return@forEach`를 작성하면 된다.

하지만, break, continue를 사용할 때엔 가급적 익숙한 for문 사용을 추천한다.

**코틀린에는 Label이란 기능이 있다.**

> Label : 특정 expression에 라벨이름@을 붙여 하나의 라벨로 간주하고 break, continue, return 등을 사용하는 기능

```kotlin
loop@ for (i in 1..100) {
    for (j in 1..100) {
        if (j == 2) {
            break@loop
        }
    }
}
```

위와 같이 작성하면 첫 번째 for 문을 중지시킬 수 있다.

하지만, 라벨을 사용한 Jump는 사용하지 않는 것은 강력 추천한다.

### 4. TakeIf와 TakeUnless

```kotlin
fun getNumberOrNull(): Int? {
    return if (numer <= 0) {
        null
    } else {
        number
    }
}
```

코틀린에서는 method chaining을 위한 특이한 함수를 제공한다.
위와 같은 함수는 `takeIf`, `takeUnless`를 사용해서 아래와 같이 변경할 수 있다. 

```kotlin
fun getNumberOrNull() {
    return number.takeIf { it > 0 }
}
```

> takeIf : 주어진 조건을 만족하면 그 값이, 그렇지 않으면 null이 반환

```kotlin
fun getNumberOrNull(): Int? {
    return number.takeUnless { it <= 0 }
}
```

> takeUnless : 주어진 조건을 만족하지 않으면 그 값이 그렇지 않으면 null이 반환된다.