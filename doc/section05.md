## 5강. 코틀린에서 조건문을 다루는 방법

### 목차

1. [if문](#1)
2. [Expression과 Statement](#2-비교-연산자와-동등성-동일성)
3. [switch와 when](#3-논리-연산자--코틀린에-있는-특이한-연산자)

### 1. if문

```java
  public void validateScoreIsNegative(int score) {
    if (score < 0) {
      throw new IllegalArgumentException(String.format("$s는 0보다 작을 수 없습니다.", score));
    }
  }
```

위 코드를 코틀린으로 변경하면

```kotlin
fun validateScoreIsNegative(score: Int) {
    if (score < 0) {
        throw IllegalArgumentException("${score}는 0보다 작을 수 없습니다.")
    }
}
```
- 자바와 달리 Unit(void)가 생략
- new를 사용하지 않고 예외를 던진다.

즉, if문 자체는 자바와 차이가 없다. 

```java
    public String getPassOrFail(int score) {
        if (score >= 50) {
            return "P";
        } else {
            return "F";
        }
    }
```

위 처럼 else문이 존재하는 경우는?

```kotlin
fun getPassOrFail(score: Int): String {
    if (score >= 50) {
        return "P"
    } else {
        return "F"
    }
}
```

마찬가지로 자바와 같지만 한 가지 다른점이 존재한다.

### 2. Expression과 Statement

자바에서 if-else는 **Statement**이지만, 코틀린에서는 **Expression**이다.
- Statement: 프로그램의 문장, 하나의 값으로 도출되지 않는다.
- Expression: 하나의 값으로 도출되는 문장

즉, Statement가 Expression을 포함하는 관계라고 할 수 있다. Statement 중에 하나의 값으로 도출되는 문장들이 Expression이다.
예를 들어 `30 + 40`은 70이라는 하나의 결과가 나온다. 따라서 Expression이면서 Statement가 되는것

따라서 코틀린에서는 위 코드를 아래와 같이 변경할 수 있다.

```kotlin
fun getPassOrFail(score: Int): String {
    return if (score >= 50) {
        "P"
    } else {
        return "F"
    }
}
```
- if부터 else 블락까지 전체를 한 번에 return
- if-elseif-else도 마찬가지

#### 어떠한 값이 특정 범위에 포함되어 있는지, 포함되어 있지 않은지

`if (0 <= score && score <= 100) { ... }`

위 코드를 코틀린으로 변경하면

```kotlin
if (score in 0..100) {}
```

### 3. switch와 when

```java
    private String getGradeWithSwitch(int score) {
        switch (score / 10) {
            case 9:
                return "A";
            case 8:
                return "B";
            case 7:
                return "C";
            default:
                return "D";
        }
    }
```

위 코드를 코틀린으로 변경하면

```kotlin
fun getGradeWithSwitch(score: Int): String {
    return when (score / 10) {
        9 -> "A"
        8 -> "B"
        7 -> "C"
        else -> "D"
    }
}
```
- 코틀린에서는 switch문이 when절로 변경되었다.
- when도 if-else와 마찬가지로 Expression이다.

문법적인 차이 외에도 코틀린의 when은 조금 더 다양한 사용이 가능하다.

```kotlin
fun getGradeWithSwitch(score: Int): String {
    return when (score) {
        in 90..99 -> "A"
        in 80..89 -> "B"
        in 70..79 -> "C"
        else -> "D"
    }
}
```
- 자바에서는 특정한 값과 일치하는 분기만을 지원했지만 코틀린에서는 특정 범위에 있거나 기타 조건을 사용해서 분기가 가능해진다.

```text
when(값) {
    조건부 -> 구문
    조건부 -> 구분
    else -> 구문
}
```

#### 조건부에는 어떠한 Expression이라도 들어갈 수 있다.

```java
private boolean startsWithA(Object obj) {
    if (obj instanceof String) {
        return ((String) obj).startsWith("A");
    } else {
        return false;
    }
}
```

위와 같은 코드를 코틀린으로 변경하면

```kotlin
fun startsWithA(any: Any): Boolean {
    return when (any) {
        is String -> any.startsWith("A")
        else -> false
    }
}
```

#### 여러 개의 조건을 동시에 검사할 수 있다. (`,`로 구분)

```java
    private void judgeNumber(int number) {
        if (number == 1 || number == 0 || number == -1) {
            System.out.println("어디서 많이 본 숫자입니다.");
        } else {
            System.out.println("1, 0, -1이 아닙니다.");
        }
    }
```

위와 같은 코드를 코틀린으로 변경하면

```kotlin
fun judgeNumber(number: Int) {
    when (number) {
        -1, 0, 1 -> println("어디서 많이 본 숫자입니다.")
        else -> println("1, 0, -1이 아닙니다.")
    }
}
```

#### when 괄호에 값 자체가 없을 수도 있다.

```java
    private void judgeNumber2(int number) {
        if (number == 0) {
            System.out.println("주어진 숫자는 0입니다");
            return;
        }

        if (number % 2 == 0) {
            System.out.println("주어진 숫자는 짝수입니다");
            return;
        }

        System.out.println("주어지는 숫자는 홀수입니다");
    }
```

위와 같은 코드를 코틀린으로 변경하면

```kotlin
fun judgeNumber2(number: Int) {
    when {
        number == 0 -> println("주어진 숫자는 0입니다")
        number % 2 == 0 -> println("주어진 숫자는 짝수입니다")
        else -> println("주어지는 숫자는 홀수입니다")
    }
}
```
- 한 줄 한 줄이 마치 Java의 early return처럼 동작시킬 수 있다.

따라서 코틀린의 when은 자바의 swith보다 더욱 유연하게 동작시킬 수 있다.
또한, when은 Enum Class 혹은 Sealed Class와 함께 사용할 경우, 더욱 더 진가를 발휘한다.

