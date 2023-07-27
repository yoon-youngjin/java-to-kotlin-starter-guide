## 6강. 코틀린에서 반복문을 다루는 방법

### 목차

1. [for-each문](#1-for-each문)
2. [전통적인 for문](#2-전통적인-for문)
3. [Progression과 Range](#3-progression과-range)
4. [while문](#4-while문)

### 1. for-each문

숫자가 들어 있는 리스트를 하나씩 출력하는 예제

```java
List<Long> numbers = Arrays.asList(1L, 2L, 3L);
for (long number : numbers) {
    System.out.println(number);
}
```

위 코드를 코틀린으로 변경하면

```kotlin
val numbers = listOf(1L, 2L, 3L)
for (number in numbers) {
    println(number)
}
```
- `:` 대신 `in`을 사용한다.


### 2. 전통적인 for문

```java
for (int i = 1; i <= 3; i++) {
    System.out.println(i);
}
```

위 코드를 코틀린으로 변경하면

```kotlin
for (i in 1..3) {
    println(i)
}
```

내려가는 경우는?

```kotlin
for (i in 3 downTo 1) {
    println(i)
}
```

2칸씩 올라가는 경우는?

```kotlin
for (i in 1..5 step 2) {
    println(i)
}
```

### 3. Progression과 Range

동작 원리?

`..`연산자는 범위를 만들어 내는 연산자이다. 

![image](https://github.com/yoon-youngjin/java-to-kotlin-starter-guide/assets/83503188/ea63f016-df9d-4696-9123-bf42ee69c72d)
- `..`연산자는 Range라는 클래스로 Progression(등차수열) 클래스를 상속받고 있다.

![image](https://github.com/yoon-youngjin/java-to-kotlin-starter-guide/assets/83503188/78a28d89-7ef4-48e8-a90c-319af1b7e5d0)
- IntRange는 IntProgression를 상속받고 있으며, step의 default값으로 1
- 즉, `1..3`의 의미는 "1에서 시작하고 3으로 끝나는 등차수열을 만들어 줘, 공차는 1"라는 의미이다.
- `3 downTo 1`: 시작값 3, 끝값 1, 공차가 -1인 등차수열
- `1..5 step 2`: 시작값 1, 끝값 5, 공차가 2인 등차수열

참고로 downTo, step은 함수이다. 중위 호출 함수로 함수를 호출하는 방법을 다르게 해준 것뿐이다. 
`변수. 함수이름(argument)` 대신 `변수 함수이름 argument`

`1..5 step 2`
1. `1..5`: 1부터 5까지 공차가 1인 등차수열 생성
2. `step 2`: 등차수열에 대한 함수 호출, `등차수열.step(2)`

결과적으로 코틀린에서 전통적은 for문은 등차수열을 이용한다.

### 4. while문

```java
int i = 1;
while (i <= 3) {
    System.out.println(i);
    i++;
}
```

위 코드를 코틀린으로 변경하면

```kotlin
var i = 1
while (i <= 3) {
    println(i)
    i++
}
```
- while문은 자바와 동일하다. 
- do-while도 마찬가지