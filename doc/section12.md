## 12강. 코틀린에서 object 키워드를 다루는 방법

코틀린에서는 자바에서 존재하지 않았던 `object`라는 별도의 지시어가 추가되었다.

### 목차

1. [static 함수와 변수](#1-static-함수와-변수)
2. [싱글톤](#2-싱글톤)
3. [익명 클래스](#3-익명-클래스)

### 1. static 함수와 변수

```java
public class JavaPerson {

  private static final int MIN_AGE = 1;

  public static JavaPerson newBaby(String name) {
    return new JavaPerson(name, MIN_AGE);
  }

  private String name;

  private int age;

  private JavaPerson(String name, int age) {
    this.name = name;
    this.age = age;
  }

}
```

위 코드를 코틀린으로 변경하면

```kotlin
class Person private constructor(
    private var name: String,
    private var age: Int,
) {
    companion object {
        private val MIN_AGE = 1
        fun newBaby(name: String): Person {
            return Person(name, MIN_AGE)
        }
    }
}
```
- 코틀린에서는 `static` 키워드가 사라지고, `companion object` 블록이 존재한다.
  - static: 클래스가 인스턴스화 될 때 새로운 값이 복제되는게 아닌 정적으로 인스턴스끼리의 값을 공유한다는 의미
  - companion object: 클래스와 동행하는 유일한 오브젝트, 인스턴스끼리 공유

현재 위 코드에서 `private val MIN_AGE`에 warning 표시가 존재하는데, 이는 `const`라는 키워드를 붙이지 않았기 때문이다.
단순히 `val`로만 쓰면 `MIN_AGE` 변수에 런타임 시에 기본값(0)이 할당되는데, 이때 `const`를 붙이게 되면 컴파일 시에 값이 할당된다.

`private const val MIN_AGE = 1`

따라서, 진짜 상수에 붙이기 위한 용도이다. 기본 타입과 `String`에만 붙일 수 있다. 

```kotlin
fun main() {
    println(Person.newBaby("윤영진"))
}
```
- 사용법은 자바와 동일

자바와 다른점이 한 가지 더 존재한다.
`companion object`, 즉 동반객체도 하나의 객체로 간주된다. 때문에 이름을 붙일 수도 있고, interface를 구현할 수도 있다.

```kotlin
class Person private constructor(
    private var name: String,
    private var age: Int,
) {
    companion object Factory: Log {
        private const val MIN_AGE = 1
        fun newBaby(name: String): Person {
            return Person(name, MIN_AGE)
        }

        override fun log() {
            println("log ...")
        }
    }
}
```

`companion object`를 통해 유틸성 함수들을 넣어도 되지만, 최상단 파일을 활용하는 것을 추천한다.

자바에서 코틀린에 있는 `static` field나 `static` 함수를 사용하고 싶은 두 가지 경우가 있을 수 있다.

**`companion object`에 이름이 없는 경우**

```java
public class Lec12Main {
  public static void main(String[] args) {
    Person.Companion.newBaby("윤영진");
  }
}

---

class Person private constructor(
        private var name: String,
        private var age: Int,
        ) {
  companion object : Log {
    private const val MIN_AGE = 1
            
    @JvmStatic
    fun newBaby(name: String): Person {
      return Person(name, MIN_AGE)
    }

    override fun log() {
      println("log ...")
    }
  }
}

public class Lec12Main {

  public static void main(String[] args) {
    Person.newBaby("윤영진");
  }

}
```
- 이름을 안 쓴 경우에는 `Companion` 이라는 이름이 생략된 경우이므로 `Companion`을 직접 명시할 수 있다. 
- 혹은 `@JvmStatic` 어노테이션을 사용하면 자바에서 `static` field나 `static` 함수를 사용하는 것 처럼 바로 접근할 수 있다.

**`companion object`에 이름이 있는 경우**

```java
public class Lec12Main {

  public static void main(String[] args) {
    Person.Factory.newBaby("윤영진");
    Person.newBaby("윤영진");
  }
}
```
- `클래스.객체명.메서드`로 접근이 가능하다.
- 또한, `@JvmStatic`을 사용하면 `클래스.메서드명`으로 접근이 가능해진다.

### 2. 싱글톤

```java
public class JavaSingleton {

  private static final JavaSingleton INSTANCE = new JavaSingleton();

  private JavaSingleton() { }

  public static JavaSingleton getInstance() {
    return INSTANCE;
  }

}
```

가장 간단한 싱글톤을 자바에서는 위와 같이 구현할 수 있다. 물론, 동시성 처리르 위해 코드를 처리하거나 enum class를 활용하는 방법도 존재한다.

위 코드를 코틀린으로 변경하면

```kotlin
object Singleton
```

```kotlin
object Singleton {
    var a: Int = 0
}
```

위 코드에 위와 같이 `a`라는 변수가 존재한다면 이를 사용하는 방법은

```kotlin
fun main() {
    println(Singleton.a)
    Singleton.a += 10
}
```
- 싱글톤은 유일한 객체이므로 `이름.변수`로 접근하면 된다.

### 3. 익명 클래스

익명 클래스는 특정 인터페이스나 클래스를 상속받은 구현체를 일회성으로 사용할 때 쓰는 클래스를 의미한다.

```java
public class Lec12Main {

  public static void main(String[] args) {
    moveSomething(new Movable() {
      @Override
      public void move() {
        ...
      }

      @Override
      public void fly() {
        ...
      }
    });
    
  }
  
  private static void moveSomething(Movable movable) {
    movable.move();
    movable.fly();
  }
}
```

위 코드를 코틀린으로 변경하면

```kotlin
fun main() {
    moveSomething(object : Movable {
        override fun move() {
            ...
        }

        override fun fly() {
            ...
        }

    })
}

private fun moveSomething(movable: Movable) {
    movable.move()
    movable.fly()
}
```
- 자바에서는 `new 타입이름()`로 구현하던 익명 클래스를 코틀린에서는 `object: 타입이름`로 변경되었다.
