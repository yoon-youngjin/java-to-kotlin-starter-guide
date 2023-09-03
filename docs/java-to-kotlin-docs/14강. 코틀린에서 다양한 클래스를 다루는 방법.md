## 14강. 코틀린에서 다양한 클래스를 다루는 방법

### 목차

1. [Data Class](#1-중첩-클래스의-종류)
2. [Enum Class](#2-enum-class)
3. [Sealed Class, Sealed Interface](#3-sealed-class-sealed-interface)

### 1. Data Class

```java
public class JavaPersonDto {

    private final String name;
    private final int age;

    public JavaPersonDto(String name, int age) {
        this.name = name;
        this.age = age;
    }
}
```

계층 간의 데이터를 전달하기 위한 DTO(Data Transfer Object)에는 일반적으로 데이터(필드), 생성자와 getter, `equals`, `hashcode`, `toString`이 존재한다.

```java
public class JavaPersonDto {

  private final String name;
  private final int age;

  public JavaPersonDto(String name, int age) {
    this.name = name;
    this.age = age;
  }

  public String getName() {
    return name;
  }

  public int getAge() {
    return age;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    JavaPersonDto that = (JavaPersonDto) o;
    return age == that.age && Objects.equals(name, that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, age);
  }

  @Override
  public String toString() {
    return "JavaPersonDto{" +
        "name='" + name + '\'' +
        ", age=" + age +
        '}';
  }
}
```

위와 같은 여러 함수를 IDE를 활용하거나 lombok을 통해 생성할 수 있지만 클래스가 장황해지거나, 클래스 생성 이후 추가적인 처리를 해줘야 하는 단점이 있다.

이를 코틀린으로 변경하면

```kotlin
data class PersonDto (
    val name: String,
    val age: Int
)
```

위와 같이 **data class**를 만들면 자동으로 `equals`, `hashcode`, `toString`을 자동으로 생성해준다. 

여기서 Dto를 만들 때 `named argument`를 활용하면 builder pattern을 쓰는 것 같은 효과를 가져갈 수 있다.

> 자바에서는 JDK16부터 코틀린의 data class 같은 record class를 도입

### 2. Enum Class

```java
public enum JavaCountry {

  KOREA("KO"),
  AMERICA("US");

  private final String code;

  JavaCountry(String code) {
    this.code = code;
  }

  public String getCode() {
    return code;
  }

}
```

위와 같은 `enum class`의 특징으로 추가적인 클래스를 상속받을 수 없으며, 인터페이스는 구현할 수 있고, 각 코드(KOREA, AMERICA)는 싱글톤이 된다.

이를 코틀린으로 변경하면

```kotlin
enum class Country(
    private val code: String,
) {
    KOREA("KO"),
    AMERICA("US")
}
```

`when`은 Enum Class 혹은 Sealed Class와 함께 사용할 경우 더욱더 진가를 발휘한다.

```java
private static void handleCountry(JavaCountry country) {
    if (country == JavaCountry.KOREA) {
      // 로직 처리
    }

    if (country == JavaCountry.AMERICA) {
      // 로직 처리
    }
  }
```

자바에서는 위와 같이 Enum 객체를 받아서 각 타입에 따라 분기문을 작성한다. 

이러한 경우 코드가 많아지게 되면 상대적으로 가독성이 떨어지고, else 로직 처리가 애매해진다.

이러한 경우 코틀린에서 `when`을 사용한다면

```kotlin
fun handleCountry(country: Country) {
    when (country) {
        Country.KOREA -> TODO()
        Country.AMERICA -> TODO()
    }
}
```

Enum Class는 컴파일 타임에 어떤 코드가 존재하는지 전부 알고 있기 때문에 when에서 Enum을 소괄호로 값으로 받을 때는 country 값이 어떤 게 있는지 다 알고 있기 때문에 추가적으로 else를 작성해 줄 필요가 없어진다.

또한, Country Enum Class에 추가되는 경우에 when 절에서 IDE에서 warning이 발생한다.
하지만 자바에서는 알 수 없다. 


### 3. Sealed Class, Sealed Interface

> sealed: 봉인을 한 

상속이 가능하도록 추상클래스를 만들려고 하는데 추상 클래스를 만들면 외부에서 누군가가 가져다가 또 상속이 가능해진다.
이런 경우를 만들고 싶지 않은 경우, 상속이 가능하게끔 계층 구성을 하고 싶지만 외부에서는 이 클래스를 상속받지 않았으면 좋겠다는 니즈에서 **Sealed Class**가 탄생했다.

즉, 우리가 작성한 클래스만 하위 클래스가 되도록 봉인하는 의미

**Sealed Class 특징**

1. 컴파일 타임 때 하위 클래스의 타입을 모두 기억한다. 즉, 런타임 때 클래스 타입이 추가될 수 없다.
2. 하위 클래스는 같은 패키지에 있어야 한다.

**Enum과 다른점**
1. Sealed Class는 클래스를 상속받을 수 있다. 
2. 하위 클래스는 멀티 인스턴스가 가능하다.

```kotlin
sealed class HyundaiCar(
    val name: String,
    val price: Long
)

class Avante : HyundaiCar("아반떼", 1000L)

class Sonata : HyundaiCar("소나타", 2000L)

class Grandeur : HyundaiCar("그렌저", 3000L)
```

컴파일 타임 때 하위 클래스의 타입을 모두 기억한다. 즉, 런타임때 클래스 타입이 추가될 수 없다.
이러한 특징으로 인해 Enum Class와 마찬가지로 when expression을 활용할 수 있다.

```kotlin
fun handleCar(car: HyundaiCar) {
    when (car) {
        is Avante -> TODO()
        is Grandeur -> TODO()
        is Sonata -> TODO()
    }
}
```
- `is 타입`으로 분기 처리가 가능하다.

**추상화가 필요한 Entity or DTO에 sealed class를 활용할 수 있다.**

> 추가로, JDK17 에서도 Sealed Class가 추가되었다.

