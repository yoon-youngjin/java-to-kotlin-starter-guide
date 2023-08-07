## 16강. 코틀린에서 배열과 컬렉션을 다루는 방법

### 목차

1. [배열](#1-배열)
2. [코틀린에서의 Collection - List, Set, Map](#2-코틀린에서의-collection---list-set-map)
3. [컬렉션의 null 가능성, Java와 함께 사용하기](#3-컬렉션의-null-가능성-java와-함께-사용하기)

### 1. 배열

배열은 프로덕션 코드에서 잘 사용하지 않는다. **배열보단 리스트**

```java
int[] arr = {100, 200};

for (int i = 0; i < arr.length; i++) {
    System.out.println("%s %s", i, arr[i]);
}
```

위 코드를 코틀린으로 변경하면

```kotlin
fun main() {

    val arr = arrayOf(100, 200)

    for (i in arr.indices) {
        println("${i} ${arr[i]}")
    }

    for ((idx, value) in arr.withIndex()) {
        println("$idx $value")
    }

    arr.plus(300)
}
```
- `arr.indices`는 0부터 마지막 index까지의 Range이다.
- `arr.withIndex()`는 인덱스와 값을 한 번에 가져올 수 있다. 
- `arr.plus()`를 통해 배열에 쉽게 값을 넣을 수도 있다. 


### 2. 코틀린에서의 Collection - List, Set, Map

컬렉션을 만들어줄 때 **불변인지, 가변인지**를 설정해야 한다.

![image](https://github.com/yoon-youngjin/java-to-kotlin-starter-guide/assets/83503188/66600b74-892c-46b9-8e73-491fb29232eb)

#### List

**불변 컬렉션(List, ...)과 가변 컬렉션(MutableList, ...)의 차이**
- 가변 컬렉션: 컬렉션에 element를 추가, 삭제할 수 있다.
- 불변 컬렉션: 컬렉션에 element를 추가, 삭제할 수 없다. 

불변 컬렉션이라 하더라도 Reference Type인 Element의 필드는 바꿀 수 있다. 

![image](https://github.com/yoon-youngjin/java-to-kotlin-starter-guide/assets/83503188/bb599a59-502d-4886-be01-2c1d63e3beed)
- 불변 리스트가 위와 같은 상황인 경우 새로운 Money 객체를 넣을 수 없고, 기존의 Money 객체를 뺄 수도 없다. 
- 하지만 예를 들어, 첫번째 Money 객체에 접근(`list.get(0)`)하여 안에 존재하는 필드(`price`)를 변경하는 것은 가능하다. 

```java
final List<Integer> numbers = Arrays.asList(100, 200);
```

위 코드를 코틀린으로 변경하면

```kotlin
val numbers = listOf(100, 200)
val emptyList = emptyList<Int>()
```
- 코틀린에서는 `listOf`를 통해 불변 리스트를 만든다.
- `emptyList<타입>()`을 통해 비어있는 리스트를 만들 수도 있다. 
  - 만약 `emptyList`에서 타입 추론이 가능하다면 `<타입>`을 생략할 수도 있다.

```kotlin
private fun printNumbers(numbers: List<Int>) {
    ...
}

fun main() {
    printNumbers(emptyList())
}
```

```java
System.out.println(numbers.get(0));

// For Each
for (int number : numbers) {
    System.out.println(number);
}

// 전통적인 For문
for (int i = 0; i < numbers.size(); i++) {
    System.out.println("%s %s", i, numbers.get(i));
}
```

위 코드를 코틀린으로 변경하면

```kotlin
fun main() {
    val numbers = listOf(100, 200)

    println(numbers[0])
    println(numbers.get(0))

    for (number in numbers) {
        println(number)
    }

    for ((idx, value) in numbers.withIndex()) {
        println("$idx $value")
    }

}
```

위에서 만든 List는 기본적으로 불변이다. 가변 List를 만들고 싶다면 `listOf()` 대신에 `mutableListOf()`를 사용하면 된다. 

```kotlin
val nums = mutableListOf(100, 200)
nums.add(300)
```
- `mutableListOf()`의 기본 구현체는 `ArrayList`
  - 기본 사용법은 자바와 동일하다. 

> Tip!
> 
> 우선 불변 리스트를 만들고, 꼭 필요한 경우 가변 리스트로 변경하자.

#### Set

집합은 List와 다르게 순서가 없고, 같은 element는 하나만 존재할 수 있다. 

자료구조적인 의미만 제외하면 모든 기능이 List와 비슷하다. 

```kotlin
fun main() {
    
    val numberSet = setOf(100, 200)

    // For Each
    for (number in numberSet) {
        println(number)
    }

    // 전통적인 For문
    for ((idx, number) in numberSet.withIndex()) {
        println("$idx $number")
    }
}
```

가변 집합을 만들고 싶은 경우에는 `mutableSetOf()`를 사용하면 된다.

```kotlin
val numbers = mutableSetOf(100, 200)
```
- `mutableSetOf()`의 기본 구현체는 LinkedHashSet
  - 입력 순서를 보장한다. 

#### Map

```java
// JDK 8까지
Map<Integer, String> map = new HashMap<>();
map.put(1, "MONDAY");
map.put(2. "TUSEDAY");

// JDK 9부터
Map.of(1, "MONDAY", 2, "TUESDAY");
```

위 코드를 코틀린으로 변경하면

```kotlin
val oldMap = mutableMapOf<Int, String>()
oldMap.put(1, "MONDAY")
oldMap[1] = "MONDAY"
oldMap[2] = "TUESDAY"
    
mapOf(1 to "MONDAY", 2 to "TUESDAY")
```
- 자바와 동일하게 `put()`을 사용할 수도 있고, `map[key] = value`도 가능하다.
- `map(key to value, ...)`를 사용하여 불변 맵을 생성할 수듀 있다. 

```java
for (int key : map.keySet()) {
    System.out.println(key);
    System.out.println(map.get(key));
}

for (Map.Entry<Integer, String> entry : map.entrySet()) {
    System.out.println(entry.getKey());
    System.out.println(entry.getValue());
}
```

위 코드를 코틀린으로 변경하면

```kotlin
for (key in oldMap.keys) {
  println(key)
  println(oldMap[key])
}

for ((key, value) in oldMap.entries) {
  println(key)
  println(value)
}
```

### 3. 컬렉션의 null 가능성, Java와 함께 사용하기

#### 컬렉션의 null 가능성

`List<Int?>`, `List<Int>?`, `List<Int?>?` 차이점
- `List<Int?>`: 리스트에 `null`이 들어갈 수 있지만, 리스트는 절대 `null`이 아니다.
- `List<Int>?`: 리스트에 `null`이 들어갈 수 없고, 리스트는 `null`일 수 있다.
- `List<Int?>?`: 리스트에 `null`이 들어갈 수 있고, 리스트도 `null`일 수 있다.

즉, `?`위치에 따라 `null` 가능성 의미가 달라지므로 차이를 잘 이해해야 한다. 

#### 자바와 코틀린이 컬렉션을 함께 사용할 때 주의할 점

**자바는 읽기 전용 컬렉션과 변경 가능 컬렉션을 구분하지 않는다.** 

즉,코틀린에서 생성한 불변 리스트를 자바에서 가져와서 element를 추가할 수 있다. 그 후에 코틀린에 돌아가면 자바에 의해 불변 리스트에 element가 추가됨으로 인해 오동작을 야기할 수 있다.

**자바는 `nullable` 타입과 `non-nullable` 타입을 구분하지 않는다.**

코틀린에서 생성한 `non-nullable` 리스트(`List<Int>`)를 자바에서 가져가서 `null`을 리스트에 추가한 뒤, 코틀린으로 돌아가면 `non nullable` 리스트에 `null`이 들어감으로써 오동작을 야기할 수 있다.

따라서 위와 같은 문제를 해결하기 위해서는 코틀린 쪽의 컬렉션이 자바에서 호출되면 컬렉션 내용이 변할 수 있음을 감안해야 한다. 
즉, 코틀린 쪽의 코드가 자바에서 불릴 수 있다면 다시 돌아왔을 경우에 방어 로직을 짜는 등의 처리가 필요하다.

그것이 아니라면 코틀린 쪽에서 `Collections.unmodifableXXX()`을 활용하면 변경 자체를 막을 수는 있다. 그렇게 하여 자바에서 가져가도 변경하지 못하도록 막을 수 있다. 


**코틀린에서 자바 컬렉션을 가져다 사용할 때 플랫폼 타입을 신경써야 한다.**


예를 들어, 자바에서 `List<Integer>`라는 타입 컬렉션을 코틀린으로 보낸 경우에 코틀린 입장에서는 `List<Int?>`인지, `List<Int>?`인지, `List<Int?>?`인지 알 수가 없다. 
이러한 경우에는 자바 코드를 보며, 맥락을 확인하고 자바 코드를 가져오는 지점을 wrapping 해야 한다. 




