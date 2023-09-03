## 7강. 코틀린에서 예외를 다루는 방법

### 목차

1. [try catch finally 구문](#1-try-catch-finally-구문)
2. [Checked Exception과 Unchecked Exception](#2-checked-exception과-unchecked-exception)
3. [try with resources](#3-try-with-resources)

### 1. try catch finally 구문

주어진 문자열을 정수로 변경하는 예제

```java
private int parseIntOrThrow(@NotNull String str) {
    try {
      return Integer.parseInt(str);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException(String.format("주어진 $s는 숫자가 아닙니다.", str));
    }
}
```

위 코드를 코틀린으로 변경하면

```kotlin
fun parseIntOrThrow(str: String): Int {
    try {
        return str.toInt()
    } catch (e: NumberFormatException) {
        throw IllegalArgumentException("주아진 ${str}은 숫자가 아닙니다.")
    }
}
```
- 기본 타입간의 형번환은 `toType()`을 사용한다.

주어진 문자열을 정수로 변경하는 예제는 동일하고 실패하면 null을 반환

```java
private Integer parseIntOrThrowV2(@NotNull String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return null;
        }
}
```

위 코드를 코틀린으로 변경하면

```kotlin
fun parseIntOrThrowV2(str: String): Int? {
    return try {
        str.toInt()
    } catch (e: NumberFormatException) {
        null
    }
}
```
- try-catch도 하나의 Expression으로 간주한다.
  - 따라서, try-catch의 최종 결과물을 반환할 수 있다. 
- try-catch-finally 역시 자바와 코틀린이 동일하다.

### 2. Checked Exception과 Unchecked Exception

프로젝트 내 파일의 내용물을 읽어오는 예제

```java
private void readFile() throws IOException {
        File currentFile = new File(".");
        File file = new File(currentFile.getAbsoluteFile() + " /a.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        System.out.println(reader.readLine());
        reader.close();
}
```
- 현재 파일의 위치를 찾은 다음, 절대 위치를 기준으로 a.txt라는 파일을 가져오는 예제
- 자바에서는 `throws IOException`을 하지않으면 컴파일 에러가 발생하는데, 이유는 `close()`, `new FileReader()`, `readLine()` 들이 `IOException`을 던지기 때문이다.
  - `IOException`은 CheckedException으로 예외 처리를 강제한다.

위 코드를 코틀린으로 변경하면

```kotlin
fun readFile() {
    val currentFile = File(".")
    val file = File(currentFile.absolutePath + "/a.txt")
    val reader = BufferedReader(FileReader(file))
    println(reader.readLine())
    reader.close()
}
```
- 코틀린에서는 자바와 달리 `FileReader()`, `readLine()`, `close()`가 `IOException`을 던지고 있는데도 불구하고, thorws는 강제시키지 않는다.
  - 코틀린에서는 Checked Exception과 Unchecked Exception을 구분하지 않는다.
  - 모두 Unchecked Exception 간주한다.
- 자바에서는 try-catch로 Checked Exception을 잡아서 Unchecked Exception으로 래핑하는 등의 작업이 불필요해진다. 

### 3. try with resources

프로젝트 내 파일의 내용물을 읽어오는 예제


```java
public void readFile(String path) throws IOException {
    try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
      System.out.println(reader.readLine());
    }
}
```

위 코드를 코틀린으로 변경하면

```kotlin
fun readFile(path: String) {
  BufferedReader(FileReader(path)).use { reader ->
    println(reader.readLine())
  }
}
```
- 코틀린에서는 try with resources가 없다.
- BufferedReader의 확장 함수인 `use`를 사용한다.