## 1강. 코틀린에서 변수를 다루는 방법

### 목차

1. [변수 선언 키워드 - var과 val의 차이점](#1-변수-선언-키워드---var과-val의-차이점)
2. [Kotlin에서의 Primitive Type](#2-kotlin에서의-primitive-type)
3. [Kotlin에서의 nullable 변수](#3-kotlin에서의-nullable-변수)
4. [Kotlin에서의 객체 인스턴스화](#4-kotlin에서의-객체-인스턴스화)

### 1. 변수 선언 키워드 - var과 val의 차이점

```java
long number1 = 10L; // (1)
final long number2 = 10L; // (2)

Long number3 = 1_000L; // (3)
Person person = new Person("윤영진"); // (4)
```

**Java에서 `long`과 `fianl long`의 차이는 해당 변수가 가변인가?, 불변인가? (read-only)**

```kotlin
var number1 = 10L
val number2 = 10L
```
- var : Variable의 약자 (발)
  - 선언된 이후에 값 변경 가능
- val : Value의 약자 (밸)
  - 선언된 이후에 값 변경 불가능

코틀린에서는 모든 변수에 수정 가능 여부 (`var` / `val`)를 명시해주어야 한다.

코틀린에서는 타입을 자동으로 컴파일러가 추론 해주기 때문에 타입을 의무적으로 작성하지 않아도 되지만 원한다면 `var number1: Long = 10L` 과 같이 타입을 작성할 수 있다.

### 초기값을 지정해주지 않은 경우는?

```kotlin
var number1 
```
- 위 코드는 값을 넣지 않았기 때문에 컴파일러가 타입을 추론하지 못해서 컴파일 에러가 발생한다.
- 따라서 위 코드의 경우에는 명시적으로 타입을 작성해줘야 한다. `var number1 : Long`

```kotlin
var number1: Long
println(number1)
```
- 위 코드는 초기화되지 않은 값을 사용하기 때문에 컴파일 에러가 발생한다.
- `number1 = 5L` 초기화를 진행하면 오류가 사라진다.

```kotlin
val number2: Long
number2 = 10L
println(number2)
```
- `val`은 불변이지만 아직 초기화되지 않은 변수에 한해서는 최초 한번은 값을 할당할 수 있다.

### val 컬렉션에는 element를 추가할 수 있다.
```java
final List<Integer> numbers = Arrays.asList(1, 2);
// numbers = Arrays.asList(3, 4);
numbers.add(4);
```
- 자바에서 불변 컬렉션을 새로운 컬렉션을 바꾸는 것은 불가능하지만 해당 컬렉션안에 새로운 element를 추가하는 것은 가능한데, 코틀린도 마찬가지다.

```kotlin
val numbers: List<Int> = listOf(1, 2)
```

### 간단한 Tip

**모든 변수는 우선 val로 만들고 꼭 필요한 경우 var로 변경한다.**
- 모든 변수를 우선 불변으로 만들고 꼭 필요한 경우에만 가변으로 만드는 식으로 코드를 작성하면 코드를 깔끔하게 작성할 수 있으며, 디버깅도 수월해진다.

## 2. Kotlin에서의 Primitive Type

```java
long number1 = 10L; // (1)
Long number3 = 1_000L; // (3)
```

위 코드를 코틀린으로 옮기면

```kotlin
var number1 = 10L
var number3 = 1_000L
```
- 자바에서는 `long`은 primitive type / `Long`은 reference type 다르게 구분
- 코틀린에서는 별도의 구분이 없다. 
- 코틀린에서는 숫자, 문자, 불리언과 같은 몇몇 타입은 내부적으로 특별한 표현을 갖는다. 이 타입들은 실행시에 Primitive Value로 표현되지만, 코드에서는 평범한 클래스처럼 보인다.
  - `Long`타입 하나로 합쳐져 있지만 만약 연산을 하게 될 경우에는 코틀린이 내부적으로 상황에 따라 Primitive, Reference Type으로 바꿔서 처리해준다.
  - 즉, 프로그래머가 boxing / unboxing을 고려하지 않아도 되도록 Kotlin이 알아서 처리해준다.

## 3. Kotlin에서의 nullable 변수

```java
Long number3 = 1_000L; 
```
- 자바에서 `Long`타입이 갖는 의미 중 또 다른 의미에는 해당 변수에는 `null`이 들어갈 수 있다는 뜻도 존재한다.

```kotlin
var number5 = 10L
number5 = null // 오류

```
- 코틀린에서는 자바와 다르게 `null이 들어갈 수 있는`을 아예 다르게 간주한다.
- 코틀린에서는 기본적으로 모든 변수를 `null`이 들어갈 수 없게끔 설계되어 있기 때문에 오류가 발생한다.
- 따라서 `null`을 넣어주고 싶다면 `var number5: Long? = 10L` 이처럼 `타입?`를 사용해야 한다.

## 4. Kotlin에서의 객체 인스턴스화

```java
Person person = new Person("윤영진");
```

위 코드를 코틀린으로 옮기면

```kotlin
var person = Person("윤영진")
```
- 자바에서는 객체를 인스턴스화를 할 때 `new`키워드를 사용하지만 코틀린에서는 `new`를 붙이지 않아야 한다.

