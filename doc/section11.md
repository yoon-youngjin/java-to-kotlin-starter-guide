## 11강. 코틀린에서 접근 제어를 다루는 방법

### 목차

1. [자바와 코틀린의 가시성 제어](#1-자바와-코틀린의-가시성-제어)
2. [코틀린 파일의 접근 제어](#2-코틀린-파일의-접근-제어)
3. [다양한 구성요소의 접근 제어](#3-다양한-구성요소의-접근-제어)
4. [자바와 코틀린을 함께 사용할 경우 주의할 점](#4-자바와-코틀린을-함께-사용할-경우-주의할-점)

### 1. 자바와 코틀린의 가시성 제어

**자바의 접근 제어**
- `public`: 모든 곳에서 접근 가능
- `protected`: 같은 패키지 또는 하위 클래스에서만 접근 가능
- `default`: 같은 패키지에서만 접근 가능
- `private`: 선언된 클래스 내에서만 접근 가능

**코틀린의 접근 제어**
- `public`: 모든 곳에서 접근 가능
- `protected`: 선언된 클래스 또는 하위 클래스에서만 접근 가능
- `internal`: 같은 모듈에서만 접근 가능
- `private`: 선언된 클래스 내에서만 접근 가능

**달라진 점**
- `protected`: 자바에서는 하위 클래스 및 본인 자신, 같은 패키지에서 `protected`를 접근할 수 있었지만 코틀린에서는 같은 패키지에서 접근이 불가능해졌다.
  - 코틀린에서는 패키지를 **namespace**를 관리하기 위한 용도로만 사용한다. 즉, 가시성 제어에는 사용되지 않는다.
  - 어떤 클래스가 어떤 패키지에 있는지에 대한 영역을 나누기 위한 용도로만 사용한다. 가시성 제어, "같은 패키지에 있으니까 너네끼리는 소통할 수 있어"와 같은 용도로는 사용하지 않는다.
- `default`: `default`가 삭제되었다.
- `internal`: 패키지 자체를 가시성 제어로 사용하지 않기 때문에 `internal`이라는 새로운 가시성 제어 지시자가 생겼다.
  - 모듈: 한 번에 컴파일 되는 코틀린 코드 -> IDEA Module, Gradle Source Set, Maven Project, ..
- 자바의 기본 접근 지시어는 `default`이고, 코틀린의 기본 접근 지시어는 `public`


### 2. 코틀린 파일의 접근 제어

코틀린은 `.kt`파일에 변수, 함수, 클래스 여러 개를 바로 만들 수 있다.

```kotlin
val a = 3

fun add(a: Int, b: Int): Int = a + b

class test()
```

이러한 파일에 접근 제어 지시어를 붙이는 경우 

1. `public`: 기본값, 어디서든 접근이 가능해진다.
2. `protected`: 파일(최상단)에는 **사용 불가능**

![image](https://github.com/yoon-youngjin/java-to-kotlin-starter-guide/assets/83503188/a7b57cf1-46fc-48d2-ac1d-c950035bb7ff)
- 코틀린에서 `protected`는 선언된 클래스와 하위 클래스에서 작동하는 지시어이기 때문에 사용 불가능 

3. `internal`: 같은 모듈에서만 접근 가능 
4. `private`: 같은 파일에서만 접근 가능 

### 3. 다양한 구성요소의 접근 제어

클래스, 생성자, 프로퍼티에 접근 지시어 

#### 클래스 안의 멤버

- `public`: 모든 곳에서 접근 가능
- `protected`: 선언된 클래스 또는 하위 클래스에서만 접근 가능
- `internal`: 같은 모듈에서만 접근 가능
- `private`: 선언된 클래스 내에서만 접근 가능

#### 생성자 

생성자도 가시성 범위는 동일하다. 단, 생성장에 접근 지시어를 붙이려면, `constructor`를 써야 한다.

```kotlin
class test internal constructor(
    val price: Int
)
```
- 기본값으로 `public constructor`가 생략된 구조 

자바에서 유틸성 코드를 만드는 방법 중 하나로 `abstract class` + `private constructor`를 사용할 수 있다.

```java
public abstract class StringUtils {

  private StringUtils() {
  }

  public boolean isDirectoryPath(String path) {
    return path.endsWith("/");
  }
}
```

이를 코틀린에서도 동일하게 작성할 수 있지만, 파일 최상단에 바로 유틸 함수를 작성하면 편하다.

`StringUtils.kt`

```kotlin
fun isDirectoryPath(path: String): Boolean {
    return path.endsWith("/")
}
```

위와 같이 코틀린에서는 직접 파일에 유틸성 코드를 작성해서 사용하는게 편하다. 이를 디컴파일해보면

![image](https://github.com/yoon-youngjin/java-to-kotlin-starter-guide/assets/83503188/1c1d537b-0d36-4d9b-b588-59e705a6c7a0)
- `StringUtilsKt`라는 클래스가 생기고, 클래스안에 `static` 함수로 `isDirectoryPath`가 존재 

#### 프로퍼티

프로퍼티도 가시성 범위는 동일하다. 단, 프로퍼티의 가시성을 설정 해주는 방법에 두 가지가 존재한다.

```kotlin
class Car(
  internal val name: String,
  private var owner: String,
  _price: Int
) {
  var price = _price
    private set
}
```
- `name` 프로퍼티의 getter를 `internal`로 지정
- `owner` 프로퍼티의 getter, setter를 `private`로 지정
- `price` 프로퍼티의 getter는 `public`, setter는 `private`으로 지정


1. `val` 혹은 `var` 앞에 바로 접근 지시어를 붙혀서 getter나 setter 둘 다 한 번에 접근 지시어를 정하는 방법
2. 만약에 setter는 `private`으로 두고 싶은 경우에 `private set`과 같이 접근 지시어를 정해줄 수 있다.

### 4. 자바와 코틀린을 함께 사용할 경우 주의할 점

- `internal`은 바이트 코드 상 `public`이 된다. 때문에 자바 코드에서는 코틀린 모듈의 `internal` 코드를 가져올 수 있다.
  - 예륻 들어서, 상위 모듈이 자바로 이루어졌고, 하위 모듈이 코틀린이라면 하위 모듈에 있는 `internal` 로 지정된 필드나 함수 등을 상위 모듈에서 바로 가져올 수 있다.
- 코틀린의 `proctected`와 자바의 `protected`는 다르다.
  - 자바는 같은 패키지의 코틀린 `protected` 멤버에 접근할 수 있다.


