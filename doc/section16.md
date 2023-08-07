## 16강. 코틀린에서 다양한 함수를 다루는 방법

### 목차

1. [확장 함수](#1-확장-함수)
2. [infix 함수](#2-infix-함수)
3. [inline 함수](#3-inline-함수)
4. [지역함수](#4-지역함수)

### 1. 확장 함수

코틀린은 자바와 **100% 호환하는 것**을 목표로 하고 있다.
이러한 목표로 인해 생긴 고민은 `기존 자바 코드 위에 자연스럽게 코틀린 코드를 추가할 수 없을까?`

자바 코드는 그대로 둔 상태로 추가적인 기능을 만든다면 val, var, null-safety 같은 코틀린의 특성을 사용할 수 있기 때문이다.
즉, 자바로 만들어진 라이브러리를 유지보수, 확장할 때 코틀린 코드를 덧붙이고 싶다.

이런 니즈를 충족하는 방법으로 어떤 클래스안에 있는 메서드처럼 호출할 수 있지만, 함수의 코드 자체는 클래스 밖에 존재하는 **확장함수**가 등장하였다. 

```kotlin
fun String.lastChar(): Char {
    return this[this.length - 1]
}
```
- `String.`: String 클래스를 확장한다.
- `lastChar()`: 함수 이름 
- `this`: `this`를 통해 인스턴스에 접근 가능
- `fun 확장하려는클래스.함수이름(파라미터): 리턴타입 { //this를 이용해 실제 클래스 안의 값에 접근 }`
  - 이때 `this`를 수신객체라고 부른다.
  - `확장하려는클래스`는 수신객체 타입이라고 한다. 

```kotlin
fun main() {
    val str = "ABC"
    println(str.lastChar())
}
```

**확장함수가 `public`이고, 확장함수에서 수신객체클래스의 `private` 함수를 가져오면 캡슐화가 깨진다?**

애당초 확장함수는 클래스에 있는 `private` 또는 `protected` 멤버를 가져올 수 없다. 

**멤버함수와 확장함수의 시그니처가 같다면?**

```java
public class Person {

  private final String firstName;
  private final String lastName;
  private int age;

  public Person(String firstName, String lastName, int age) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.age = age;
  }

  public String getFirstName() {
    return firstName;
  }

  public int getAge() {
    return age;
  }

  public int nextYearAge() {
    System.out.println("멤버 함수");
    return this.age + 1;
  }

}
```

```kotlin
fun Person.nextYearAge(): Int {
    println("확장 함수")
    return this.age + 1
}

fun main() {
    val person = Person("윤", "영진", 100)
    println(person.nextYearAge())
}
```
- 이러한 경우에 멤버 함수가 우선적으로 호출된다.
- 따라서, 확장 함수를 만들었지만, 다른 기능의 똑같은 멤버 함수가 생긴다면 오류가 발생할 수 있다.

**확장 함수가 오버라이드 된다면?**

```kotlin
open class Train(
    val name: String = "새마을기차",
    val price: Int = 5000,
)

fun Train.isExpensive(): Boolean {
    println("Train의 확장 함수")
    return this.price >= 10000
}

class Srt : Train("SRT", 40000)

fun Srt.isExpensive(): Boolean {
    println("Srt의 확장 함수")
    return this.price >= 10000
}

```

```kotlin
fun main() {
    val train: Train = Train()
    train.isExpensive() // Train의 확장 함수

    val srt1: Train = Srt()
    srt1.isExpensive() // Train의 확장 함수

    val srt2: Srt = Srt()
    srt2.isExpensive() // Srt의 확장 함수
}
```
- 해당 변수의 현재 타입 즉, 정적인 타입에 의해 어떤 확장 함수가 호출딜지 결정된다.

**정리**
1. 확장 함수는 원본 클래스의 private, protected 멤버 접근이 안된다.
2. 동일한 시그니처의 멤버 함수, 확장 함수 중 멤버 함수가 우선권이 있다. 
3. 확장 함수는 현재 타입을 기준으로 호출된다.

**자바에서 코틀린 확장 함수를 가져다 사용할 수 있나?**

```java
public static void main(String[] args) {
    Lec16MainKt.lastChar("ABC");    
}
```
- 정적 메소드를 부르는 것처럼 사용 가능하다. 

**확장 함수라는 개념은 확장 프로퍼티와도 연결된다.**

확장 프로퍼티의 원리는 확장함수 + custom getter

```kotlin
fun String.lastChar(): Char {
    return this[this.length - 1]
}

val String.lastChar: Char
    get() = this[this.length - 1]
```

### 2. infix 함수

증위 함수, 함수를 호출하는 새로운 방법

기존의 `변수.함수이름(argument)` 대신 `변수 함수이름 argument` (argument가 하나만 존재해야 한다.)

```kotlin
fun Int.add(other: Int): Int {
    return this + other
}

infix fun Int.add2(other: Int): Int {
    return this + other
}

fun main() {
  println(3.add(4))
  
  println(3.add2(4))
  println(3 add2 4)
}
```

**Infix는 멤버 함수에도 붙일 수 있다.**

### 3. inline 함수

함수가 호출되는 대신, 함수를 호출한 지점에 함수 본문을 그대로 복사하는 것

```kotlin
inline fun Int.add3(other: Int): Int {
    return this + other
}

3.add3(4)
```
- `inline`이 붙은 `add3`를 바이트 코드로 바꾸면 원래는 `add3`라는 함수도 바이트 상에서 새로 생기고, `3.add3(4)`가 바이트상의 함수를 호출하는 식이어야 하는데,

![image](https://github.com/yoon-youngjin/java-to-kotlin-starter-guide/assets/83503188/93f5e5c2-5b90-42b8-b924-8d8eaa2f9015)
- 함수를 호출하는 게 아니라 덧셈하는 로직 자체가 해당 함수를 부르는 쪽에 복사 붙여하기가 된다.

이러한 특성을 통해 함수를 파라미터로 전달할 때에 발생하는 오버헤드를 줄일 수 있다.

> 함수를 계속 중첩해서 쓰는 경우에는 해당 함수가 또 다시 다른 함수를 부르고 또 다른 함수를 부르면 call chain에 overhead가 발생

하지만 inline 함수의 사용은 성능 측정과 함께 신중하게 사용해야 한다.

### 4. 지역함수

함수 안에 함수를 선언할 수 있다. 

```kotlin
fun createPerson(firstName: String, lastName: String): Person {
    if (firstName.isEmpty()) {
        throw IllegalArgumentException("firstName은 비어있을 수 없습니다! 현재 값 : $firstName")
    }

    if (lastName.isEmpty()) {
        throw IllegalArgumentException("lastName은 비어있을 수 없습니다! 현재 값 : $lastName")
    }

    return Person(firstName, lastName, 1)
}
```
- 위에 if문의 중복이 발생했음을 알 수 있다. 

위와 같은 코드를 변경하면

```kotlin
fun createPerson(firstName: String, lastName: String): Person {
    fun validateName(name: String, fieldName: String) {
        if (name.isEmpty()) {
            throw IllegalArgumentException("${fieldName}은 비어있을 수 없습니다! 현재 값 : $name")
        }
    }
    validateName(firstName, "firstName")
    validateName(lastName, "lastName")

    return Person(firstName, lastName, 1)
}
```

반복으로 인해 함수로 추출하면 좋을 것 같은데, 이 함수를 지금 함수 내에서만 사용하고 싶은 경우 지역함수를 사욯할 수 있다.

하지만 코드의 depth가 깊어지고, 코드가 깔끔하지 않기 때문에 위와 같은 경우에는 Person 클래스에서 private validation 코드를 통해 검증하는 것이 깔끔해질 수 있다.
