## 9강. 코틀린에서 클래스를 다루는 방법

### 목차

1. [클래스와 프로퍼티](#1-클래스와-프로퍼티)
2. [생성자와 init](#2-생성자와-init)
3. [커스텀 getter, setter](#3-커스텀-getter-setter)
4. [backing field](#4-backing-field)

### 1. 클래스와 프로퍼티

개명이 불가능한 나라에서 사는 Person 클래스 예시 

```java
public class JavaPerson {

  private final String name;
  private int age;
  
}
```
- 위같은 자바 코드에서는 컴파일 에러가 발생한다.
  - 불변 필드(`name`)의 값을 설정해줄 수 없기 때문이다.
  - 문제를 해결하기 위해 불변 필드를 포함하는 생성자를 만들어야 한다.

```java
public class JavaPerson {

    private final String name;
    private int age;

    public JavaPerson(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
```
- `name`은 불변이기 때문에 setter를 만들 수 없다. 

위 코드를 코틀린으로 변경하면

```kotlin
class Person constructor(name: String, age: Int) {

    val name: String = name
    var age: Int = age

}
```
- 프로퍼티 = 필드 + getter + setter
- 코틀린에서는 필드(`val name = name`)만 만들면 getter, setter를 자동으로 만들어준다.

```kotlin
class Person(
  val name: String,
  var age: Int
)


fun main() {
  val person = Person("윤영진", 27)
  println(person.name)
  person.age = 28
}

```
- `constructor` 키워드를 생략할 수 있다.
- 코틀린에서는 생성자를 만들어줄 때 동시에 프로퍼티를 선언할 수 있다.
- 클래스 바디에 아무런 내용이 없으면 생략할 수 있다.
- `.필드`를 통해 getter와 setter를 바로 호출할 수 있다.

코틀린에서 자바 클래스를 가져다 쓰는 경우에도 `.필드`를 통해 getter, setter를 사용할 수 있다.

```kotlin
package yoon.lec09

import com.lannstark.lec09.JavaPerson

fun main() {
    val javaPerson = JavaPerson("윤영진", 27)
    println(javaPerson.name)
    javaPerson.age = 28
}
```


### 2. 생성자와 init

클래스가 생성되는 시점에 나이를 검증하는 예제

```java
public JavaPerson(String name, int age) {
        if (age <= 0) {
            throw new IllegalArgumentException(String.format("나이는 %s일 수 없습니다.", age));
        }
        this.name = name;
        this.age = age;
}
```

위와 같이 자바에서는 생성자에서 검증을 진행할 수 있다. 그렇다면 코를린에서는?

```kotlin
class Person(
    val name: String,
    var age: Int
) {
    init {
        if (age <= 0) {
            throw IllegalArgumentException("나이는 ${age}일 수 없습니다.")
        }
    }
}
```
- 코틀린에서는 init 블럭이 존재하는데, 이는 생성자가 호출되는 시점에 단 한번만 함께 호출되는 블럭이다.
- 코틀린에서는 클래스가 생성되는 시점에 검증 로직을 넣고 싶다면 init 블럭을 사용해볼 수 있다.

요구사항이 추가되어서 최초로 태어나는 아기는 무조건 1살이니, 생성자를 추가해야 하는 경우?

```java
public JavaPerson(String name) {
        this(name, 1);
    }
```

위 코드를 코틀린으로 변경하면

```kotlin
class Person(
    val name: String,
    var age: Int
) {
    init {
        if (age <= 0) {
            throw IllegalArgumentException("나이는 ${age}일 수 없습니다.")
        }
    }
    
    constructor(name: String): this(name, 1)
}

val person2 = Person("윤영진")
```
- `constructor` 키워드를 통해 별도의 생성자를 만들 수 있다. 
- `this`는 자바와 마찬가지로 다른 생성자를 가리킬 수 있다. 
  - 현재는 주생성자(primary constructor)를 가리키고 있다. 
- 주생성자는 반드시 존재해야 한다.
  - 단, 주생성자에 파라미터가 하나도 없다면 생략 가능하다. (`class Student`) 
- `constructor(파라미터)`로 생성한 생성자는 부생성자(secondary constructor)라 한다.
  - 최종적으로 주생성자를 this로 호출해야 한다. (`constructor(): this("윤영진")`)
  - body를 가질 수 있다. (`constructor(name: String): this(name, 1) { println(name) })`

생성자를 호출할 떄 바디는 역순으로 실행된다.

```kotlin
class Person(
    val name: String,
    var age: Int
) {
    init {
        if (age <= 0) {
            throw IllegalArgumentException("나이는 ${age}일 수 없습니다.")
        }
        println("초기화 블록")
    }

    constructor(name: String): this(name, 1) {
        println("부생성자 1") 
    }
    constructor(): this("윤영진") {
        println("부생성자 2")
    }
}

val person3 = Person()

```

![image](https://github.com/yoon-youngjin/java-to-kotlin-starter-guide/assets/83503188/d2ca97cb-8df9-4f5b-9ccd-e65bda54f4cd)

하지만 코틀린에서는 부생성자보다는 **default parameter**를 권장한다.

```kotlin
class Person(
    val name: String = "윤영진",
    var age: Int = 27
) {
  init {
    if (age <= 0) {
      throw IllegalArgumentException("나이는 ${age}일 수 없습니다.")
    }
    println("초기화 블록")
  }
}
```

컨버팅(어떤 객체를 다른 객체로 바꾸는 것)이 필요한 경우 부생성자를 사용해볼 수 있지만, 그보다는 정적 팩토리 메서드를 추천한다.
따라서, 왠만하면 부생성자를 사용하지 않는다.


### 3. 커스텀 getter, setter

#### 성인인지를 확인하는 기능을 추가하는 예제 

```java
public boolean isAdult() {
        return this.age >= 20;
}
```

위 코드를 코틀린으로 변경하면

```kotlin
fun isAdult(): Boolean {
        return this.age >= 20
}
```

위처럼 자바와 동일한 방법도 존재하지만 다른 방법도 존재한다.

```kotlin
val isAdult: Boolean
  get() = this.age >= 20

val isAdult: Boolen
  get() {
    return this.age >= 20
  }
```
- **custom getter**라고 한다. 마치 해당 클래스에 프로퍼티가 있는 것처럼 보여주는 것이다.
- 클라이언트가 isAdult를 get했을 때 실행할 로직을 작성한다.  
  - 코틀린에서는 함수처럼 만드는 방법과 직접 정의한 getter(custom getter)를 활용해 프로퍼티처럼 보이게 하는 두가지 방법이 존재한다.  
- 위 방법들과 함수로 정의한 방법을 모두 디컴파일해보면 동일한 모습임을 확인할 수 있다.

모두 동일한 기능이고 표현 방법만 다른데, 어떤 방법이 가장 좋을까?

객체의 속성이라면, custom getter 그렇지 않다면 함수를 사용한다. 예를 들어서 isAdult는 마치 해당 사람이 성인인가라는 속성을 확인하는 것이므로 **custom getter**

custom getter를 사용하면 자기 자신을 변경해 줄 수도 있다. 예를 들어, name을 get할 때 무조건 대문자로 바꾸는 예시

#### name을 set할 때 무조건 대문자로 바꾸는 예시

```kotlin
class Person(
    name: String = "윤영진",
    var age: Int = 27
) {
  var name = name
    set(value) {
      field = value.uppercase()
    }
  ...
}
```
- setter를 사용해야 하므로 `var`로 변경

하지만 Setter 자체를 지양하기 때문에 custom setter도 많이 사용하지 않는다. 무분별한 setter 보다는 update와 같은 함수를 만든다.


### 4. backing field

custom getter를 사용하면 자기 자신을 변경해 줄 수도 있다. 예를 들어, name을 get할 때 무조건 대문자로 바꾸는 예시

```kotlin
class Person(
    name: String = "윤영진",
    var age: Int = 27
) {

  val name = name
    get() = field.uppercase()
      
  ...
}
```
- `val name` 혹은 `var name`이라고 선언하게 되면 하나의 프로퍼티이기 때문에 getter를 자동으로 만들어주기 때문에 우선 `val`을 제거한다.
- `name`에 대한 Custom getter를 만들 때 `field` 키워드를 사용해야 한다.
  - `get() = name.uppercase()`를 사용한다고 하면 밖에서 `person.name`을 호출하면 `name`에 대한 getter가 호출된다.
  - 똑같이 안에서도 `name`이라고 하면 `name`에 대한 getter가 호출된다.
  - 즉, `name.uppercase()`의 `name`은 `name`에 대한 getter를 호출하니까 다시 get을 부른다. -> 무한루프가 발생
  - 무한루프를 막기 위한 예약어, 자기 자신을 가리키는 `field`라는 키워드를 사용한다. 즉, 진짜 name을 가리킨다. 

이러한 `field`를 자기 자신을 가리키는 보이지 않는 `field`라고 하여 **backing field**라고 부른다.

하지만, custom getter에서 backing field를 쓰는 경우는 드물다. 방금과 같은 요구사항을 아래와 같이 해결할 수 있다.

```kotlin
class Person(
  val name: String = "윤영진",
  var age: Int = 27
) {

  fun getUppercaseName(): String = this.name.uppercase()

  val upperCaseName: String
    get() = this.name.uppercase()
  
      ...
}
```

위처럼 작성하면 마치 밖에서 볼 때는 원하는 요구사항을 달성하는 프로퍼티가 하나 더 있는 것처럼 보여줄 수 있기 때문에 backing field를 사용해서 custom getter를 만들 일이 많지 않을 수 있다.
