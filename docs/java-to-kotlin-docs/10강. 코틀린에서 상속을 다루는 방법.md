## 10강. 코틀린에서 상속을 다루는 방법

### 목차

1. [추상 클래스](#1-추상-클래스)
2. [인터페이스](#2-인터페이스)
3. [클래스를 상속할 때 주의할 점](#3-클래스를-상속할-때-주의할-점)
4. [상속 관련 지시어 정리](#4-상속-관련-지시어-정리)

### 1. 추상 클래스

`Animal`이란 추상 클래스를 구현한 `Cat`, `Penguin`

```java
public abstract class JavaAnimal {

  protected final String species;
  protected final int legCount;

  public JavaAnimal(String species, int legCount) {
    this.species = species;
    this.legCount = legCount;
  }

  abstract public void move();

  public String getSpecies() {
    return species;
  }

  public int getLegCount() {
    return legCount;
  }

}
```

위 코드를 코틀린으로 변경하면

```kotlin
abstract class Animal(
    protected val species: String,
    protected val legCount: Int
) {
    abstract fun move()
}
```

```java
public class JavaCat extends JavaAnimal {

  public JavaCat(String species) {
    super(species, 4);
  }

  @Override
  public void move() {
    System.out.println("고양이가 사뿐 사뿐 걸어가~");
  }

}
```

위 코드를 코틀린으로 변경하면

```kotlin
class Cat(
    species: String,
) : Animal(species, 4) {
    
    override fun move() {
        println("고양이가 사뿐 사뿐 걸어가~")
    }
}
```
- 자바와 달리 `extends` 키워드를 사용하지 않고 `:`를 사용한다.
- 코틀린에서는 어떤 클래스를 상속받을 때 무조건 상위 클래스의 생성자를 바로 호출해야 한다. -> `Animal(species, 4)`
- 코틀린에서는 `override`를 필수적으로 붙여 주어야 한다.

```java
public final class JavaPenguin extends JavaAnimal {

  private final int wingCount;

  public JavaPenguin(String species) {
    super(species, 2);
    this.wingCount = 2;
  }

  @Override
  public void move() {
    System.out.println("펭귄이 움직입니다~ 꿱꿱");
  }

  @Override
  public int getLegCount() {
    return super.legCount + this.wingCount;
  }

  @Override
  public void act() {
    JavaSwimable.super.act();
    JavaFlyable.super.act();
  }

}
```

위 코드를 코틀린으로 변경하면

```kotlin
class Penguin(
    species: String,
) : Animal(species, 2) {

    private val wingCount: Int = 2

    override fun move() {
        println("펭귄이 움직입니다~ 꿱꿱")
    }

    override val legCount: Int
        get() = super.legCount + this.wingCount
}
```
- 위와 같이 작성하면 `Animal`에 존재하는 `legCount`는 `final`이기 때문에 `override`가 불가능하다는 에러가 발생한다.
  - 해당 컴파일 에러는 `Animal` 추상 클래스의 `legCount`에 `open`을 붙혀서 해결할 수 있다.
  - `abstract class Animal(protected val species: String, protected open val legCount: Int)`
  - 코틀린에서는 자바와 다르게 프로퍼티를 `override`할 때 반드시 `open`을 붙여줘야 한다.

자바와 코틀린 모두 추상 클래스는 인스턴스화 할 수 없다. 

### 2. 인터페이스

`Flyable`과 `Swimmable`을 구현한 `Penguin`

```java
public interface JavaFlyable {

  default void act() {
    System.out.println("파닥 파닥");
  }

  void fly();
}
```

```java
public interface JavaSwimable {

  default void act() {
    System.out.println("어푸 어푸");
  }

}
```

위 코드를 코틀린으로 변경하면

```kotlin
interface Flyable {

  fun act() {
    println("파닥 파닥")
  }

  fun fly()
}
```

```kotlin
interface Swimable {

  fun act() {
    println("어푸 어푸")
  }
}
```
- 코틀린에서는 `default` 키워드 없이 메소드 구현이 가능하다. 

```java
public final class JavaPenguin extends JavaAnimal implements JavaSwimable, JavaFlyable {

  private final int wingCount;

  public JavaPenguin(String species) {
    super(species, 2);
    this.wingCount = 2;
  }

  @Override
  public void move() {
    System.out.println("펭귄이 움직입니다~ 꿱꿱");
  }

  @Override
  public int getLegCount() {
    return super.legCount + this.wingCount;
  }

  @Override
  public void act() {
    JavaSwimable.super.act();
    JavaFlyable.super.act();
  }

  @Override
  public void fly() {
    ...
  }

}
```

위 코드를 코틀린으로 변경하면

```kotlin
class Penguin(
    species: String,
) : Animal(species, 2), Flyable, Swimable {

    private val wingCount: Int = 2

    override fun move() {
        println("펭귄이 움직입니다~ 꿱꿱")
    }

    override val legCount: Int
        get() = super.legCount + this.wingCount

    override fun act() {
        super<Swimable>.act()
        super<Flyable>.act()
    }
    override fun fly() {
    }

}
```
- 코틀린에서는 인터페이스 구현에도 `:`을 사용한다. 
- 자바에서의 `인터페이스.super.메서드()` 문법이 코틀린에서는 `super<인터페이스>.메서드()`로 변경 

자바와 코틀린 모두 인터페이스를 인스턴스화 할 수 없다. 

#### 코틀린에서는 backing field가 없는 프로퍼티를 Interface에 만들 수 있다. 

```kotlin
interface Swimable {

    val swimAbility: Int

    fun act() {
        println("어푸 어푸")
    }
}
```
- `swimAbility`이라는 프로퍼티는 `swimable` 인터페이스에 존재하는것이 아닌 `val`이기 때문에 getter에 대한 것을 아래에서 구현해주는 걸 기대하는 것이다.

```kotlin
class Penguin(
    species: String,
) : Animal(species, 2), Flyable, Swimable {

    ...
    override val swimAbility: Int
        get() = 3
}
```

위처럼 구현 클래스에서 구현을 강제하거나 직접 정의할 수도 있다. 이렇게 구현하면 해당 값이 default 값이 된다. 
이러한 경우에는 구현 클래스에서 `swimAbility`의 구현을 강제하지 않는다. 물론 재정의할 수도 있다. 

```kotlin
interface Swimable {

  val swimAbility: Int
    get() = 3

  fun act() {
    println(swimAbility)
    println("어푸 어푸")
  }
}
```

### 3. 클래스를 상속할 때 주의할 점

`Derived` 클래스가 `Base` 클래스를 상속받는 예제

```kotlin
open class Base (
    open val number: Int = 100
) {
    init {
        println("Base Class")
        println(number)
    }
}
```

```kotlin
class Derived (
    override val number: Int
): Base(number) {
    init {
        println("Derived Class")
    }
}
```
- `Derived` 클래스에서 `Base` 클래스를 상속해야 하므로 클래스 이름 앞에 `oepn`을 붙여줬다.
  - `Derived` 클래스에서 `Base` 클래스의 `number`라는 프로퍼티를 `override`

위와 같은 경우 `Derived` 클래스를 인스턴스화하면 어떻게 동작할까?
초기화 블록이 어떤 순서로 실행되며 `number`값에는 뭐가 들어갈까?

```kotlin
val derived = Derived(300)
```

![image](https://github.com/yoon-youngjin/java-to-kotlin-starter-guide/assets/83503188/0a1d2b2a-f7b0-4c2e-b5b3-8844341debc2)
- 상위 클래스의 init 블록이 먼저 실행된다.
- `number` 값에 0이 들어가 있다. -> 100, 300도 아닌 
  - 상위 클래스의 생성자가 실행되는 중에 상위 클래스에서 `number`를 호출하게 되면 하위 클래스의 `number`를 가져올텐데 이때 아직 하위 클래스에서는 `number`라는 값에 초기화가 이루어지지 않았기 때문에 발생하는 문제이다.
  - 따라서 상위 클래스에 생성자 또는 init 블록에서는 하위 클래스의 `final`이 아닌 프로퍼티에 접근하면 안 된다. 

**상위 클래스를 설계할 때 생성자 또는 초기화 블록에 사용되는 프로퍼티에는 `open`을 피해야 한다.**

### 4. 상속 관련 지시어 정리

1. `final`: `override`를 할 수 없게 한다. 기본값이 `final`이므로 일반 클래스를 다른 누군가가 상속받게 하려면 `open`을 반드시 붙혀야 한다.  
2. `open`: `override`를 열어 준다. 상속을 해도 되고 안해도 된다.
3. `abstract`: 반드시 `override` 해야 한다. 
4. `override`: 상위 타입을 오버라이드 하고 있다. 



