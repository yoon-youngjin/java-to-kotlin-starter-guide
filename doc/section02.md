## 2강. 코틀린에서 null을 다루는 방법

`코틀린은 null이 들어갈 수 있는 변수를 완전히 다르게 취급한다`

### 목차

1. [Kotlin에서의 null 체크](#1-kotlin에서의-null-체크)
2. [Safe Call과 Elvis 연산자](#2-safe-call과-elvis-연산자)
3. [null 아님 단언 !!](#3-null-아님-단언--)
4. [플랫폼 타입](#4-플랫폼-타입)

### 1. Kotlin에서의 null 체크

```java
public boolean startsWithA(String str) {
    return str.startsWith("A");
}
```

**위 코드는 안전한 코드가 아니다**

str 변수에 null이 들어올 가능성이 존재한다. 따라서 만약에 null이 들어오면 NPE가 발생할 수 있다.
위 코드를 안전하게 만들기 위해서는 아래와 같이 3가지 방법 정도가 존재할 수 있다.

```java
  public boolean startsWithA1(String str) {
    if (str == null) {
      throw new IllegalArgumentException("null이 들어왔습니다");
    }
    return str.startsWith("A");
  }


  public Boolean startsWithA2(String str) {
    if (str == null) {
      return null;
    }
    return str.startsWith("A");
  }


  public boolean startsWithA3(String str) {
    if (str == null) {
      return false;
    }
    return str.startsWith("A");
  }
```

위 코드를 코틀린으로 작성하면

```kotlin
fun startsWithA1(str: String?): Boolean {
    if (str == null) {
        throw IllegalArgumentException("null이 들어왔습니다.")
    }
    return str.startsWith("A")
}

fun startsWithA2(str: String?): Boolean? {
    if (str == null) {
        return null
    }
    return str.startsWith("A")
}

fun startsWithA3(str: String?): Boolean {
    if (str == null) {
        return false
    }
    return str.startsWith("A")
}
```
- `str`에 `null`도 들어올 수 있음을 나타내기 위해 `String?`
- `str` 변수의 null 체크를 해주었음으로 컴파일러가 `startWith()` 호출을 허락한다.

```kotlin
fun startsWithA(str: String?): Boolean {
    return str.startsWith("A")
}
```
- 위와 같은 코드가 컴파일 에러가 난다. 
  - `?`를 사용함으로써 아예 다른 타입으로 간주시켰다.
  - `str` 변수는 `null`일 수도 있는데 바로 `startsWith()` 함수를 호출하려 했기 때문이다. 
  - 코틀린에서는 `null`이 들어갈 수 있는 것에 대해서 바로 함수 호출을 못하게끔 막아두었다.

### 2. Safe Call과 Elvis 연산자

코틀린에는 `null`이 가능한 타입만을 위한 기능(**Safe Call**, **Elvis 연산자**)이 존재한다. 

### Safe Call

`null`이 아니면 실행하고, `null`이면 실행하지 않는다. (그대로 `null`)

```kotlin
val str: String? = "ABC"
str.length // 불가능
str?.length // 가능
```
- 위 코드에서 `str` 변수는 ABC 문자열로 초기화되어 있지만 실제로는 `null`이 들어갈 수 있는 변수다.
  - 따라서 `str.length`를 바로 호출할 수 없다.
  - 하지만 `str?.length`와 같이 Safe Call(`?.`)을 쓰게되면 호출이 가능해진다. 
    - Safe Call(`?.`)의 의미는 앞 변수가 `null`이 아니면 뒤의 함수나 프로퍼티를 실행시키고, `null`이면 값 그대로 `null`이 된다.

```kotlin
val str: String? = null
println(str?.length)
```
- 위 코드는 실행이 되고 `null`을 출력한다.

### Elvis 연산자 

앞의 연산 결과가 `null`이면 뒤의 값을 사용

```kotlin
val str: String? = "ABC"
str?.length ?: 0
```
- Safe Call과 함께 사용할 수 있는 기능
- 만약에 `str`이 `null`이 아닌 경우에는 그대로 함수 실행 결과를 반환하고, `null`인 경우에는 `?:` 뒤의 값을 사용

위 작성한 3가지 함수를 조금 더 코틀린스럽게 변경

```kotlin
fun startsWithA1(str: String?): Boolean {
  return str?.startsWith("A")
    ?: throw IllegalArgumentException("null이 들어왔습니다")
}

fun startsWithA2(str: String?): Boolean? {
  return str?.startsWith("A")
}

fun startsWithA3(str: String?): Boolean {
  return str?.startsWith("A")
    ?: false
}
```

**Elvis 연산은 early return에도 사용할 수 있다**

```java
public long calculate(Long number) {
    if(number == null) {
        return 0;
    }
}
```

자바에서 위와 같은 early return 코드를 코틀린에서는 if문을 사용하지 않고 Elvis 연산자를 활용할 수 있다.

```kotlin
fun calculate(number: Long?) {
  number ?: return 0
}
```

### 3. null 아님 단언 !!

nullable type이지만, 아무리 생각해도 `null`이 될 수 없는 경우에 매번 Safe Call을 사용하고 예외 처리를 계속해주는 것이 아닌 `!!`를 사용할 수 있다.

```kotlin
fun startsWith(str: String?): Boolean {
    return str!!.startsWith("A")
}
```
- `str`변수는 반드시 `null`이 아님을 컴파일러에게 알려줌으로써 컴파일 에러를 피할 수 있다.
- 만약에 `null`이 들어오는 경우에 런타임에서 `NPE`가 발생한다.

### 4. 플랫폼 타입

Koltin에서 Java코드를 가져다 사용할 때 어떤 타입이 `null`이 될 수 있는지, `null`이 될 수 없는지를 처리하는 방법

```java
public class Person {

  private final String name;

  public Person(String name) {
    this.name = name;
  }

  @Nullable
  public String getName() {
    return name;
  }
}
```

```kotlin
fun main() {
    val person = Person("윤영진")
    println(startsWith(person.name))
}
fun startsWith(str: String): Boolean {
    return str.startsWith("A")
}
```
- 위 코드는 컴파일 에러가 발생한다.
  - `@Nullable` 어노테이션으로 인해서 반환값이 `null`이 될 수 있음을 컴파일러에게 알려주었기 때문이다.
  - 따라서 코틀린에서 자바 코드를 가져다가 쓸 때는 위와 같은 `null`에 대한 어노테이션 정보를 코틀린이 이해한다.

javax.annotation 패키지, android.support.annotation 패키지, org.jetbrains.annotation 패키지의 `null`에 관련된 어노테이션을 활용하면 코틀린에서 이를 인식하고 이해해서 처리한다.

### 플랫폼 타입

코틀린이 null 관련 정보를 알 수 없는 타입으로 Runtime시 Exception이 발생할 수 있다.

```java
public class Person {

  private final String name;
  
  public Person(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
```
- `@Nullable`과 같은 `null` 관련 어노테이션을 제거하면 코틀린 코드에서 컴파일 에러가 사라지는데 이러한 경우 만약에 `null`이 들어오는 경우 Runtime시에 Exception이 발생한다.
- 그렇기 때문에 코틀린에서 자바 코드를 사용할 때는 `null` 관련된 정보를 꼼꼼히 작성하거나 아니면 애당초 그런게 작성되어 있지 않은 라이브러리를 가져다 써야 하는 경우에 라이브러리 코드를 열어서 `null` 관련된 정보를 확인하면 좋다.

Kotlin에서 Java 코드를 사용할 때 **플랫폼 타입** 사용에 유의해야 한다. 
- Java 코드를 읽으며 널 가능성을 확인하거나 Kotlin에서 wrapping하여 Java 코드를 가져오는 지점을 최소화(단일 진입점으로 만듦)해볼 수 있다. 

