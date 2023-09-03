## 8강. 코틀린에서 함수를 다루는 방법

### 목차

1. [함수 선언 문법](#1-함수-선언-문법)
2. [default parameter](#2-default-parameter)
3. [named argument (parameter)](#3-named-argument--parameter-)
4. [같은 타입의 여러 파라미터 받기 (가변인자)](#4-같은-타입의-여러-파라미터-받기--가변인자-)

### 1. 함수 선언 문법

두 정수를 받아 더 큰 정수를 반환하는 예제

```java
public int max(int a, int b) {
        if (a > b) {
            return a;
        }
        return b;
}
```

위 코드를 코틀린으로 변환하면

```kotlin
fun max(a: Int, b: Int): Int {
    return if (a > b) {
        a
    } else {
        b
    }
}
```
- 접근 지시어(public, private, protected, default)는 생략하면 public이다.
- 코틀린에서는 if-else 문이 하나의 Expression이다.
  - 함수가 하나의 결과값이면 block(중괄호) 대신 `=` 사용이 가느앟다. 

```kotlin
fun max(a: Int, b: Int): Int =
    if (a > b) {
        a
    } else {
        b
    }
```

즉, 중괄호 안에서 return으로 어떤 값을 전달한다라고 표현하는 대신에 단순히 함수의 값의 결과물을 `=`을 사용해서 바로 표현할 수 있다.

위 코드를 한번더 변환하면

```kotlin
fun max(a: Int, b: Int) = if (a > b) a else b
```
- `=`을 사용하는 경우 반환 타입 생략이 가능하다.
  - 함수에서 Int타입의 `a` 혹은 `b`를 반환하기 때문에 컴파일러가 반환값 추론이 가능하다. 따라서 반환타입을 생략할 수 있다.
  - `=`이 아닌 block({})을 사용하는 경우에는 반환 타입이 `Unit`이 아니면, 명시적으로 작성해줘야 한다.
- 한 줄로 변경이 가능하다. 


### 2. default parameter

주어진 문자열을 N번 출력하는 예제

```java
public void repeat(String str, int num, boolean useNewLine) {
        for (int i = 1; i <= num; i++) {
            if (useNewLine) {
                System.out.println(str);
            } else {
                System.out.print(str);
            }
        }
}
```

위와 같은 함수인데 많은 코드에서 `useNewLine`이라는 파라미터에 `true`을 사용한다면?

이러한 경우 매번 `useNewLine`에 true를 전달하기 보다 자바에서는 오버로딩을 활용해볼 수 있다.

```java
public void repeat(String str, int num) {
        repeat(str, num, true);
}
```

많은 코드에서 출력을 3회씩 사용하고 있다면? 즉, `num` 파라미터에 3을 자주 사용하고 있는 경우

마찬가지로 자바에서는 오버로딩을 사용해볼 수 있다.

```java
public void repeat(String str) {
        repeat(str, 3, true);
}
```

위와 같이 자바 코드에서는 메서드를 3개나 만들어야 하는 문제가 존재한다. 

코틀린에서는 이러한 부분을 해결할 수 있는 **default parameter**가 존재한다.

```kotlin
fun repeat(
    str: String,
    num: Int = 3,
    useNewLine: Boolean = true
) {
    for (i in 1..num) {
        if (useNewLine) {
            println(str)
        } else {
            print(str)
        }
    }
}
```
- 함수를 사용할 때 파라미터를 넣어주지 않으면 기본값을 사용한다.
- 물론 코틀린에도 자바와 동일하게 오버로딩을 사용할 수 있다.


### 3. named argument (parameter)

위의 예제에서 repeat 함수를 호출할 때 `num`은 그대로 3을 쓰고 `useNewLine`은 false를 쓰고 싶은 경우?

```kotlin
repeat("Hello World", 3,false)
```

위처럼 호출할 수 있겠지만 기본값을 다시 한 번 넣어주기 싫은 경우에 어떤 parameter에 어떤 값을 넣을 거라는 것을 명시해줄 수 있따.

```kotlin
repeat("Hello World", useNewLine = false)
```

이러한 것을 **named argument**라고 한다.
이를 통해서 builder를 직접 만들지 않고 builder의 장점을 가질 수 있다.

```kotlin
fun printNameAndGender(name: String, gender: String) {
    println(name)
    println(gender)
}

fun main() {
  printNameAndGender("Female", "윤영진")
}
```
- 위와 같이 파라미터의 위치를 실수로 잘못 넣는 문제를 야기할 수 있다.
- 이러한 경우 builder 패턴(`.name(...)`)을 사용하게 되면 실수를 방지할 수 있다.
- 비슷하게 코틀린에 존재하는 **named argument**를 쓰게 되면 builder의 장점을 가져갈 수 있다.

```kotlin
fun main() {
  printNameAndGender(name = "윤영진", gender = "Female")
}
```

추가적으로 코틀린에서 자바 함수를 가져다 사용할 때는 named argument를 사용할 수 없다.


### 4. 같은 타입의 여러 파라미터 받기 (가변인자)

문자열을 N개 받아 출력하는 예제

```java
public static void printAll(String... strings) {
        for (String str : strings) {
            System.out.println(str);
        }
}

public static void main(String[] args) {
        String[] arr = new String[]{"A", "B", "C"};
        printAll(arr);
        printAll("A", "B", "C", "D");
}
```
- 자바에서는 `타입...`을 사용하여 가변인자를 사용할 수 있다.
- 또한, 함수를 사용할 때는 배열을 직접 넣거나, 콤마를 이용해 여러 파라미터를 넣을 수 있다.

위 코드를 코틀린으로 변경하면

```kotlin
fun printAll(vararg strings: String) {
  for (str in strings) {
    println(str)
  }
}

fun main() {
    printAll("A", "B", "C", "D")
    
    val arr = arrayOf("A", "B", "C")
    printAll(*arr)
}
```
- 코틀린에서는 자바의 `타입...` 대신 `vararg`를 사용하며 배열이 들어오는 것은 동일하다.
- 코틀린에서 함수를 호출할 때 배열을 직접 넣는 경우에 `*`를 사용한다.
  - `*`은 spread연산자라 해서 배열 안에 있는 원솓르을 마치 콤마를 쓰는 것처럼 꺼내주는 연산자다.
