## 4강. 코틀린에서 연산자를 다루는 방법

### 목차

1. [단항 연산자 / 산술 연산자](#1-단항-연산자--산술-연산자)
2. [비교 연산자와 동등성, 동일성](#2-비교-연산자와-동등성-동일성)
3. [논리 연산자 / 코틀린에 있는 특이한 연산자](#3-논리-연산자--코틀린에-있는-특이한-연산자)
4. [연산자 오버로딩](#4-연산자-오버로딩)

### 1. 단항 연산자 / 산술 연산자

- 자바의 단항 연산자: ++, --
- 자바의 산술 연산자: +, -, *, /, %
- 자바의 산술대입 연산자: +=, -=, *=, /=, %=

**Java와 Kotlin은 완전 동일하다.**


### 2. 비교 연산자와 동등성, 동일성

- 자바의 비교 연산자: >, <, >=, <=

**Java와 Kotlin 사용법은 동일하다. 단, Java와 다르게 객체를 비교할 때 비교 연산자를 사용하면 자동으로 `compareTo`를 호출한다.**

```java
public class JavaMoney implements Comparable<JavaMoney> {

    private final long amount;

    public JavaMoney(long amount) {
        this.amount = amount;
    }

    @Override
    public int compareTo(@NotNull JavaMoney o) {
        return Long.compare(this.amount, o.amount);
    }
}

public static void main(String[] args) {
    JavaMoney money1 = new JavaMoney(1_000L);
    JavaMoney money2 = new JavaMoney(2_000L);

    if (money1.compareTo(money2) > 0) {
        System.out.println("Money1이 Money2보다 금액이 큽니다.");
    }
}
```
- `Comparable` 인터페이스를 구현해서 들어오는 `JavaMoney`와 현재 인스턴스 `JavaMoney` 간의 크기를 비교하는 코드
- 자바에서는 위와 같이 직접 구현한 `compareTo()`를 호출해서 두 객체를 비교할 수 있다.

위 코드를 코틀린으로 변경하면

```kotlin
val money1 = JavaMoney(2_000L)
val money2 = JavaMoney(1_000L)

if (money1 > money2) {
    println("Money1이 Money2보다 금액이 큽니다.")
}
```
- 코틀린에서는 비교 연산자를 사용하면 `compareTo`를 자동으로 실행해준다.

### 동등성, 동일성

- 동등성(Equality): 두 객체의 값이 같은가?
    - 자바에서는 `Object`의 `equals`를 사용해서 동등성을 확인한다.
- 동일성(Identity): 완전히 동일한 객체인가? 즉, 주소가 같은가?
    - 자바에서는 `==`를 사용해서 동일성을 확인한다.

코틀린에서는 동일성에 `===`를 사용, 동등성에 `==`를 사용한다. `==`을 사용하면 간접적으로 `eqauls`를 호출해준다.

```kotlin
val money1 = JavaMoney(2_000L)
val money2 = money1
val money3 = JavaMoney(2_000L)

println(money1 === money2) // true
println(money1 === money3) // false
println(money1 == money3) // true
```

### 3. 논리 연산자 / 코틀린에 있는 특이한 연산자

- 자바의 논리 연산자: &&(and), ||(or), !(not)

**Java와 Kotlin은 완전히 동일하다. Java처럼 Lazy 연산을 수행한다. 즉, 앞에서 결과가 확정되면 뒤 연산을 실행하지 않는다.**

### 코틀린에 있는 특이한 연산자

- in / !in
    - 컬렉션이나 범위에 포함되어 있다, 포함되어 있지 않다
- a..b
    - a부터 b 까지의 범위 객체를 생성한다.
- a[i]
    - a에서 특정 index i로 값을 가져온다
- a[i] = b
    - a의 특정 index i에 b를 넣는다

### 4. 연산자 오버로딩

Kotlin에서는 객체마다 연산자를 직접 정의할 수 있다.

```java
public class JavaMoney implements Comparable<JavaMoney> {

  private final long amount;

  public JavaMoney(long amount) {
    this.amount = amount;
  }

  public JavaMoney plus(JavaMoney other) {
    return new JavaMoney(this.amount + other.amount);
  }
}

System.out.println(money1.plus(money2));
```
- 자바에서는 `plus()`라는 메서드를 만들고 위와 같이 사용할 수 있다.

이를 코틀린으로 변경하면

```kotlin
data class Money(
    val amount: Long
) {
    operator fun plus(other: Money): Money {
        return Money(this.amount + other.amount)
    }
}

val money1 = Money(1_000L)
val money1 = Money(2_000L)
println(money1 + money2)
println(money1.plus(money)) // 물론 가능
```
- 코틀린에서는 객체마다 직접 플러스 연산, 마이너스 연산, 단항 연산, 비교 연산 등 여러 가지 연산들을 직접 정의할 수 있다.




