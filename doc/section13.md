## 13강. 코틀린에서 중첩 클래스를 다루는 방법

### 목차

1. [중첩 클래스의 종류](#1-중첩-클래스의-종류)
2. [코틀린의 중첩 클래스와 내부 클래스](#2-코틀린의-중첩-클래스와-내부-클래스)

### 1. 중첩 클래스의 종류

자바에서 중첩 클래스는 어딘가에 소속되어 있는 클래스로 여러 종류가 존재했다.

- `Static`을 사용하는 중첩 클래스: 밖에 존재하는 클래스를 직접적으로 참조할 수 없는 클래스
- `Static`을 사용하지 않는 중첩 클래스
  - 내부 클래스(Inner Class): 밖에 존재하는 클래스를 직접 참조 가능한 클래스
  - 지역 클래스(Local Class): 메서드 내부에 클래스를 정의(실제로 거의 사용하지 않음)
  - 익명 클래스(Anonymous Class): 일회성으로 사용하는 클래스

이 중에서 일반적으로 중첩 클래스라고 하면 어떤 클래스 안에 중첩된 내부 클래스와 `static`을 사용하는 중첩 클래스를 지칭한다. 

### 2. 코틀린의 중첩 클래스와 내부 클래스

```java
public class JavaHouse {

  private String address;
  private LivingRoom livingRoom;

  public JavaHouse(String address) {
    this.address = address;
    this.livingRoom = new LivingRoom(10);
  }

  public LivingRoom getLivingRoom() {
    return livingRoom;
  }

  public class LivingRoom {
    private double area;

    public LivingRoom(double area) {
      this.area = area;
    }

    public String getAddress() {
      return JavaHouse.this.address;
    }
  }

}
```
- `static`을 사용하지 않았으므로 내부(LivingRoom)에서 외부의 클래스(JavaHouse)를 참조가 가능하다.

![image](https://github.com/yoon-youngjin/java-to-kotlin-starter-guide/assets/83503188/596a2e3e-e270-4b6a-a9bb-f802b90a9c32)

내부 클래스가 외부 클래스를 참조함으로 인해 생기는 몇 가지 문제점이 존재한다.

> Effective Java - Item24, Item26
>
> 1. 내부 클래스는 숨겨진 외부 클래스 정보를 가지고 있어, 참조를 해제하지 못하는 경우 메모리 누수가 생길 수 있고, 이를 디버깅 하기 어렵다.
>
> 2. 내부 클래스의 직렬화 형태가 명확하게 정의되지 않아 직렬화에 있어 제한이 있다.
>
> 따라서, 내부 클래스보다는 클래스 안에 클래스를 만들 때는 static 클래스를 사용하라.

코틀린에서는 이러한 가이드를 충실히 따르고 있다.

```kotlin
class House(
    private var address: String,
    private var livingRoom: LivingRoom = LivingRoom(10.0)
) {
    class LivingRoom(
        private var area: Double
    )
}
```
- 코틀린에서는 별도의 `static` 키워드 없이 내부에 class를 만들면 된다.
  - 기본적으로 바깥 클래스에 대한 연결이 없는 중첩 클래스가 만들어진다.

권장되지 않는 클래스 안의 클래스(내부 클래스)

```kotlin
class House(
    var address: String,
) {
    var livingRoom = this.LivingRoom(10.0)

    inner class LivingRoom(
        private var area: Double
    ) {
        val address: String
            get() = this@House.address
    }
}

```
- `inner` 키워드를 직접 붙여야 한다.
- 바깥 클래스를 참조하기 위해서는 `this@바깔클래스명`을 사용한다.

코틀린에서는 기본적으로 바깥 클래스를 참조하지 않는다. 굳이 바깥 클래스를 참조하고 시다면 `inner` 키워드를 추가한다.
