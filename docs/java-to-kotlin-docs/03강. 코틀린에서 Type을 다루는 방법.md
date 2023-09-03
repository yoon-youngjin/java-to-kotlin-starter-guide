## 3강. 코틀린에서 Type을 다루는 방법
 
### 목차

1. [기본 타입](#1-기본-타입)
2. [타입 캐스팅](#2-타입-캐스팅)
3. [Kotlin의 3가지 특이한 타입](#3-kotlin의-3가지-특이한-타입)
4. [String Interpolation, String Indexing](#4-string-interpolation-string-indexing)

### 1. 기본 타입

Byte, Short, Int, Long, Float, Double, 부호 없는 정수들

```kotlin
val num1 = 3 // Int
val num2 = 3L // Long

val num3 = 3.0f // Float
val num4 = 3.0 // Double
```
- 코틀린에서는 선언된 기본값(3, 3L, 3.0f, 3.0)을 보고 타입을 추론한다.

**Java와 다른 내용**
- Java: 기본 타입간의 변환은 암시적으로 이루어질 수 있다.
- Kotlin: 기본 타입간의 변환은 명시적으로 이루어져야 한다.

```java
int num1 = 4;
long num2 = num1;

System.out.println(num1 + num2);
```
- num1과 num2는 타입이 다른데도 불구하고 int는 4byte, long은 8byte로 long이 더 크니까 암시적으로 변경이 가능하다.
- Java에서는 더 큰 타입으로는 암시적으로 변경이 가능하다.

```kotlin
val num1 = 4
val num2: Long = num1 // Type mismatch

println(num1 + num2)
```
- 반면에, 코틀린에서는 위와 같은 코드는 컴파일 단계에서 에러를 발생한다.
- Kotlin에서는 암시적 타입 변경이 불가능하므로 `to변환타입()`을 사용해야 한다.

```kotlin
val num1 = 4
val num2: Long = num1.toLong()
println(num1 + num2)
```

```kotlin
val num1 = 3
val num2 = 5
val result = num1 / num2.toDouble()
```
- `정수 / 정수 = 정수` 이므로 실수의 결과를 얻고 싶은 경우에는 위와 같이 분모나 분자를 실수로 변환해줄 때 사용할 수 있다.  
  - 자바에서 `(double)`과 같은 명시적 타입 캐스팅

### 변수가 nullable이라면 적절한 처리가 필요하다.

```kotlin
val num1: Int? = 3
val num2: Long = num1.toLong()
```
- `num1`은 `null`이 들어갈 수 있는 `Int`이기 때문에 컴파일 에러를 발생한다.
  - `null`을 `Long`으로 캐스팅할 수 없기 때문
- 따라서 `val num2: Long = num1?.toLong() ?: 0L` 과 같이 Safe Call과 Elvis 연산자를 적절히 활용할 수 있다.

### 2. 타입 캐스팅

기본 타입이 아닌 일반 타입의 캐스팅

```java
  public static void printAgeIfPerson(Object obj) {
    if (obj instanceof Person) {
      Person person = (Person) obj;
      System.out.println(person.getAge());
    }
  }
```
- 자바의 `instanceof` 키워드는 변수가 주어진 타입이라면 true, 그렇지 않으면 false를 반환한다.
- 자바의 `(타입)`은 주어진 변수를 해당 타입으로 변경한다.

위 코드를 코틀린으로 옮긴다면

```kotlin
fun printAgeIfPerson(obj: Any) {
    if (obj is Person) {
        val person = obj as Person
        println(person.age)
    }
}
```
- 코틀린에서는 자바의 `intstanceof`가 아닌 `is` 키워드를 통해 타입을 확인한다.
- 코틀린에서는 자바의 `(타입)`이 아닌 `as` 키워드를 통해 캐스팅을 한다.
- 참고로 위 코드에서 `is` 키워드로 해당 변수가 `Person` 타입임을 확인했음으로 `as` 키워드를 통한 캐스팅을 생략할 수 있다. 
  - `if (obj is Person) { println(obj.age) }` 이러한 것을 **스마트 캐스트**라고 부르는데, 한번 코틀린 컴파일러가 컨텍스트를 분석해주는 기능이다. 

### `instanceof`의 반대

즉, 해당 타입이 아닌 경우 자바에서는 `if (!(obj instanceof Person))`으로 작성한다.

```kotlin
if (obj !is Person) {
    ...
}
```
- 코틀린에서는 자바와 마찬가지로 `if (!(obj is Person))`으로 작성할 수 있지만 `if (obj !is Person)`으로 작성할수도 있다.

### 만약 `obj`에 `null`이 들어올 수 있다면?

```kotlin
fun printAgeIfPerson(obj: Any?) {
    val person = obj as Person
    println(person.age)
}
```
- 위 코드는 컴파일 타임에 오류가 발생하지는 않는다.
- 하지만 Runtime에 `obj`로 `null`이 들어오는 경우 NPE가 발생한다.
- 이를 해결하기 위해서는 `as?`를 사용해야 한다.
  - `obj as? Person`: 만약 `obj`가 `null`이 아니라면 Person 타입으로 변화를 시키고 `null`이라면 Safe Call처럼 전체(`obj as? Person`)가 `null`이 된다.
  - 따라서, `null`인 경우에는 `val person = null`

**정리**
- `value is Type`
  - value가 Type이면 true
  - value가 type이 아니면 false
- `value !is Type`
  - value가 Type이면 false
  - value가 Type이 아니면 true
- `value as Type`
  - value가 Type이면 Type으로 타입 캐스팅
  - value가 Type이 아니면 예외 발생
- `value as? Type`
  - value가 Type이면 Type으로 타입 캐스팅
  - value가 null이면 null
  - value가 Type이 아니면 null
    - 기존에 `as`는 예외가 발생하지만 `as?`를 쓰게 되면 에러가 나는 대신 `null`이 나온다.

### 3. Kotlin의 3가지 특이한 타입

Any, Unit, Noting

### Any

- Java의 `Object` 역할 (모든 객체의 최상위 타입)
- 모든 Primitive Type의 최상의 타입도 `Any`이다.
  - 자바에서는 Primitive Type의 경우 각각 독립적인 타입으로 `Object`가 최상위 타입이 아니다.
  - 반면에 코틀린에서는 Primitive, Reference Type 구분 없이 모두 `Int`, `Long`, ...을 쓰기 때문에 최상위 타입이 Any로 고정된다. 
- `Any` 자체로는 `null`을 포함할 수 없어 `null`을 포함하고 싶다면, `Any?`로 표현
- `Any`에 `eqauls` / `hashcode` / `toString` 존재

### Unit

- `Unit`은 Java의 `void`와 동일한 역할
  - Java에서 함수를 작성할 때 반환할 값이 없으면 `void`를 사용한다. 
  - 마찬가지로 코틀린에서도 `Unit`을 사용할 수 있는데, 타입 추론이 가능하기 때문에 생략할 수 있다.
- 하지만 `void`와 다르게 `Unit`은 그 자체로 타입 인자로 사용 가능하다.
  - 자바의 제네릭에서 `void`는 `void` 제네릭을 쓰려면 직접 void 클래스(`Void`)를 사용해야 한다. 하지만 코틀린에서는 `Unit`을 그대로 제네릭에서 사용할 수 있다.
- 함수형 프로그래밍에서 `Unit`은 단 하나의 인스턴스만 갖는 타입을 의미. 즉, 코틀린의 `Unit`은 실제 존재하는 타입이라는 것을 표현한다.

### Noting

```kotlin
fun fail(message: String): Nothing {
    throw IllegalArgumentException(message)
}
```
- `Nothing`은 함수가 정상적으로 끝나지 않았다는 사실을 표현하는 역할
- 무조건 예외를 반환하는 함수 / 무한 루프 함수 등

### 4. String Interpolation, String Indexing

### String Interpolation

```java
Person person = new Person("윤영진", 27);
String log = String.format("사람의 이름은 %s이고 나이는 %s세 입니다.", person.getName(), person.getAge());
```

```java
StringBuilder sb = new StringBuilder();
sb.append("사람의 이름은");
sb.append(person.getName());
sb.append("이고 나이는");
sb.append(person.getAge());
```

자바에서는 위와 같이 `String.format()`을 사용하거나 `StringBuilder`를 통해서 문자열을 동적으로 원하는 형태로 가공한다.
위 코드를 코틀린으로 변경하면

```kotlin
val person = Person("윤영진", 27)
val log = "사람의 이름은 ${person.name}이고 나이는 ${person.age}세 입니다"
```
- `${변수}`를 사용하여 변수를 통해 값을 넣을 수 있다.
  - `${변수}`: Syntactic sugar

위에서 문자열과 변수를 연결할 때 `${}` 사용한다고 했는데, 코틀링 컨벤션에서는 
> Don't use curly braces when inserting a simple variable into a string template. Use curly braces only for longer expressions

라고 이야기한다. 따라서, 간단한 변수를 쓸 때 `$`를 사용하고, 긴 표현식의 경우에만 `${}`를 사용하자.

```kotlin
val name = "John"
println("Hello, $name") // 이것이 권장되는 방법
println("Hello, ${name}") // 이것은 권장되지 않는다.
```

코틀린에서는 여러 줄에 걸친 문자열을 작성해야 할 때 큰따옴표 세 개(""")를 쓰면 좀 더 편하게 사용할 수 있다.

```kotlin
val withoutIndent = 
    """
            ABC
            123
            456
    """.trimIndent()
```
- `trimIndent()`는 문자열 공백 제거하는 함수


### String Indexing

```java
String str = "ABCDE";
char ch = str.charAt(1);
```
- Java에서 문자열의 특정 문자를 가져율 때 `charAt()`함수를 사용할 수 있다.

```kotlin
val str = "ABCDE"
val ch = str[1]
```
- Kotlin에서는 대괄호를 통해서 문자열에 있는 특정 문자를 가져올 수 있다. 

