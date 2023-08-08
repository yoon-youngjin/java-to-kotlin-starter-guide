## 17강. 코틀린에서 람다를 다루는 방법

### 목차

1. [자바에서 람다를 다루기 위한 노력](#1-자바에서-람다를-다루기-위한-노력)
2. [코틀린에서의 람다](#2-코틀린에서의-람다)
3. [Closure](#3-closure)
4. [다시 try with resources](#4-다시-try-with-resources)

### 1. 자바에서 람다를 다루기 위한 노력

```java
public class Fruit {

    private final String name;
    private final int price;

    public Fruit(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public boolean isApple() {
        return this.name.equals("사과");
    }

}

List<Fruit> fruits = Arrays.asList(
            new Fruit("사과", 1_000),
            new Fruit("사과", 1_200),
            new Fruit("사과", 1_200),
            new Fruit("사과", 1_500),
            new Fruit("바나나", 3_000),
            new Fruit("바나나", 3_200),
            new Fruit("바나나", 2_500),
            new Fruit("수박", 10_000)
    );
```

위와 같은 리스트에서 **사과**만 분류하고 싶은 경우에 아래와 같이 작성할 수 있다.

```java
private List<Fruit> findApples(List<Fruit> fruits) {
        List<Fruit> apples = new ArrayList<>();
        for (Fruit fruit : fruits) {
            if (fruit.getName().equals("사과")) {
                apples.add(fruit);
            }
        }
        return apples;
}
```

이번엔 **바나나**만 분류하고 싶은 경우에

```java
private List<Fruit> findBananass(List<Fruit> fruits) {
        List<Fruit> bananas = new ArrayList<>();
        for (Fruit fruit : fruits) {
            if (fruit.getName().equals("바나나")) {
                apples.add(fruit);
            }
        }
        return apples;
}
```

중복이 존재하는걸 확인할 수 있고, 중복을 제거하기 위해 아래와 같이 리팩토링할 수 있다.

```java
private List<Fruit> findFruitsWithName(List<Fruit> fruits, String name) {
        List<Fruit> results = new ArrayList<>();
        for (Fruit fruit : fruits) {
            if (fruit.getName().equals(name)) {
                results.add(fruit);
            }
        }
        return results;
    }
```

만약에, **사과**와 **바나나**를 분류하고 싶은 경우에는? 혹은 **사과**인데 가격이 **1200원**을 넘지 않는 사과만 분류하고 싶은 경우에는?

위 코드로는 대응이 되지 않는다. 

**파라미터를 늘리는 것으로는 대응이 어렵다. -> 인터페이스와 익명 클래스를 사용하자**

```java
public interface FruitFilter {

  boolean isSelected(Fruit fruit);

}

private List<Fruit> filterFruits(List<Fruit> fruits, FruitFilter fruitFilter) {
        List<Fruit> results = new ArrayList<>();
            for (Fruit fruit : fruits) {
                if (fruitFilter.isSelected(fruit)) {
                    results.add(fruit);
                }
            }
        return results;
    }
```

```java
filterFruits(fruits, new FruitFilter() {
        @Override
        public boolean isSelected(Fruit fruit) {
            return Arrays.asList("사과", "바나나").contains(fruit.getName()) &&
                    fruit.getPrice() > 5000;
        }
    });
```

위와 같이 실제 사용할 때 인터페이스를 구현한 익명 클래스를 만듦으로써 그때 그때 필요한 필터 조건을 걸 수 있다.

하지만 **익명 클래스**를 사용하는 것은 복잡하다. 코드가 문법적으로 복잡하며, 또한 다양한 Filter가 필요할 수 있다.
예를 들어, 과일 간의 무게 비교를 한다거나, N개의 과일을 한 번에 비교한다거나 등등.. 
즉, 특정 과일을 바구니에 담거나, 담지 말거나를 제외한 다른 여러 필터 조건이 필요할 수 있다. 

이러한 불편함을 개선하기 위해 JDK8부터는 람다 (이름이 없는 함수) 등장하였다. 또한, `FruitFilter`와 같은 인터페이스 `Predicate`, `Consumner` 등을 많이 만들어 두었다.

```java
private List<Fruit> filterFruits(List<Fruit> fruits, Predicate<Fruit> fruitFilter) {
        List<Fruit> results = new ArrayList<>();
        for (Fruit fruit : fruits) {
            if (fruitFilter.test(fruit)) {
                results.add(fruit);
            }
        }
        return results;
    }
    
filterFruits(fruits, fruit -> fruit.getName().equals("사과"));
```
- `fruit -> fruit.getName().equals("사과")`는 자바에서 생긴 람다

```text
변수 -> 변수를 이용한 함수
(변수1, 변수2) -> 변수1과 변수2를 이용한 함수
```

이러한 이름 없는 함수 즉, 람다는 메서드 레퍼런스를 통해 좀 더 간결하게 작성할 수 있다.

```java
filterFruits(fruits, Fruit::isApple);
```

또한, for문과 if문을 활용한 코드를 조금 더 깔끔하고 간결하게 바꾸고 싶은 니즈를 충족하기 위해 **Stream**이 등장하였다.
이를 통해, 병렬처리에서도 강점이 생긴다. 

```java
private static List<Fruit> filterFruits(List<Fruit> fruits, Predicate<Fruit> fruitFilter) {
        return fruits.stream()
                .filter(fruitFilter)
                .collect(Collectors.toList());
    }
```

자바에서 위처럼 **메소드 자체를 직접 넘겨주는 것처럼** 쓸 수 있다.

여기서 핵심은 `것처럼`인데, 왜냐하면 실제 함수를 넘기는 것 처럼 보이지만 실제 받는 것은 인터페이스 즉, 미리 만들어뒀던 `Predicate` 인터페이스이기 때문이다.
바꿔 말하면, 자바에서 함수는 변수에 할당되거나 파라미터로 전달할 수 없다. 즉, 자바에서는 함수를 2급 시민으로 간주한다.

> 2급 시민: 변수에 할당하거나 함수의 인자로 전달하거나 반환값으로 사용하는 것이 제한적인 개체

### 2. 코틀린에서의 람다

코틀린에서 함수는 자바와 근본적으로 다른 한 가지가 있다.
코틀린에서는 함수가 그 자체로 값이 될 수 있따. 변수에 할당할수도, 파라미터로 넘길 수도 있다. (자바에서는 함수를 넘기는 것처럼 보여지지 근본적으로는 넘길 수 없었다.)

```kotlin
val fruits = listOf(
        Fruit("사과", 1000),
        Fruit("사과", 1200),
        Fruit("사과", 1200),
        Fruit("사과", 1500),
        Fruit("바나나", 3000),
        Fruit("바나나", 3200),
        Fruit("바나나", 2500),
        Fruit("수박", 10000)
    )
```

위와 같은 리스트에서 해당 과일이 사과인지를 판별하는 함수를 만들어서 변수에 선언한다면

```kotlin
// 람다를 만드는 방법 1
val isApple = fun(fruit: Fruit): Boolean {
    return fruit.name == "사과"
}

// 람다를 만드는 방법 2
val isApple2 = { fruit: Fruit -> fruit.name == "사과" }

// 람다를 직접 호출하는 방법 1
isApple(fruits[0])

// 람다를 직접 호출하는 방법 2 
isApple.invoke(fruits[0])
```
- 이름 없는 함수 즉, 람다 
- 함수 호출은 변수 자체가 함수이기 떄문에 중괄호를 사용할 수도 있고, `invoke()`를 사용할 수도 있다. 

람다에 현재 함수가 들어가있는데, 함수 역시 타입이 있다. `isApple` 같은 경우에는 `Fruit`을 인자로 받아서 Boolean을 반환하는 타입이다.

```kotlin
val isApple: (Fruit) -> Boolean = fun(fruit: Fruit): Boolean {
    return fruit.name == "사과"
}
```
- 함수의 타입: (파라미터1, 파라미터2, ...) -> 반환 타입

자바에서 작성했던 filterFruits를 코틀린으로 변경하면

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

filterFruits(fruits, isApple)

filterFruits(fruits, fun(fruit: Fruit): Boolean {
    return fruit.name == "사과"
})
```
- 자바처럼 `Predicate` 같은 인터페이스를 사용하는 것이 아닌 함수 자체를 파라미터로 받을 수 있다.

이때, `isApple2`와 같이 중괄호와 화살표를 활용하는 형태를 함수에 넣어줄 때, 함수에서 받는 함수 파라미터가 마지막에 위치해 있으면 소괄호 안에 중괄호가 들어가기 때문에 사용하는 입장에서 어색할 수 있다.
이러한 경우에는 소괄호 밖으로 중괄호를 뺄 수 있다.
또한, 현재 필터의 파라미터 타입이 `Fruit`을 받아 `Boolean`을 반환하는 것으로 함수에 들어가야 할 파라미터 타입을 추론할 수 있다. 따라서 타입 생략 가능
마지막으로 앞에 이름(`fruit`)을 명시하고 화살표를 쓰는 것도 방법이지만 코틀린에서는 익명 함수를 만들 때 파라미터가 한 개인 경우에는 `it`을 사용하면 `it`이 곧 들어오는 `Fruit`이 된다.
```kotlin
filterFruits(fruits, { fruit: Fruit -> fruit.name == "사과" })

// 변경 1
filterFruits(fruits) { fruit: Fruit -> fruit.name == "사과" }

// 변경 2
filterFruits(fruits) { fruit -> fruit.name == "사과" }

// 변경 3
filterFruits(fruits) { it.name == "사과" }
```

### 3. Closure

### 4. 다시 try with resources