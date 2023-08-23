## 20강. 코틀린의 scope function
 
### 목차

1. [scope function이란 무엇인가?](#1-scope-function이란-무엇인가)
2. [scope function의 분류](#2-scope-function의-분류)
3. [언제 어떤 scope function을 사용해야 할까?](#3-언제-어떤-scope-function을-사용해야-할까)
4. [scope function의 가독성](#4-scope-function의-가독성)

### 1. scope function이란 무엇인가?

> scope function : 일시적인 영역을 형성하는 함수 

```kotlin
fun printPerson(person: Person?) {
    if (person != null) {
        println(person.name)
        println(person.age)
    }
}
```

위 코드를 일시적인 영역과 함께 리팩토링 해보면

```kotlin
fun printPerson(person: Person?) {
    person?.let {
        println(it.name)
        println(it.age)
    }
}
```
- Safe Call (?.)을 사용 : person이 null이 아닐때에 let을 호출
- let : scope function의 한 종류, 확장함수
  - 확장함수는 내가 넣고 싶은 변수를 파라미터로 넣는 게 아니라 변수.함수

<img width="544" alt="image" src="https://github.com/yoon-youngjin/library-app/assets/83503188/4fe202b5-4f60-419d-a08b-a2530cd77a43">
- let : block 이라는 함수를 파라미터로 받는데, block은 T를 파라미터로 하고 R을 반환하는 함수, 반환타입은 block을 실행시켜 나온 R
    - 따라서 let은 함수를 인자로 받아서 함수를 실행시킨다.
    - 람다({})를 사용하고 있으며, 람다 안에서 it을 통해서 person에 접근

**scope function은 람다를 사용해 일시적인 영역을 만들고 코드를 더 간결하게 만들거나, method chaning을 활용하는 함수이다.**

### 2. scope function의 분류

scope function에는 let, run, also, apply, with 총 5가지가 존재한다.
이 중에서 with는 확장함수가 아니다. 확장함수의 특징은 마치 멤버 함수를 쓰는 것처럼 사용된다.

1. let, run : 람다의 결과를 반환한다.
2. also, apply : 객체 그 자체를 반환한다.

```kotlin
val value1 = person.let {
    it.age
}

val value2 = person.run {
    this.age
}

val value3 = person.also {
    it.age
}

val value4 = person.apply {
    this.age
}
```
- value 1, value 2에는 age가 할당된다.
- value 3, value 4에는 person이 할당된다.
- 즉, let, run은 람다의 결과가 반환되고, also와 apply는 람다의 결과와는 무관하게 객체 그 자체가 반환된다.

1. let, also : it 사용
2. run, apply : this

확장함수에 집어넣은 람다에서 확장함수의 수신 객체(person)를 호출할 때 it, this의 차이가 존재한다.
- this : 생략이 가능한 대신, 다른 이름을 붙일 수 없다.
- it : 생략이 불가능한 대신, 다른 이름을 붙일 수 있다. 

```kotlin
val value5 = person.let { p ->
  p.age
}

val value6 = person.run {
  age
}
```

**이런 차이는 왜 발생할까?**

이런 차이가 발생한 이유는 코틀린 문법 때문이다. 

<img width="508" alt="image" src="https://github.com/yoon-youngjin/library-app/assets/83503188/314d58de-1b57-4577-a336-7a860ad5e96f">

- let은 일반 함수(`(T) -> R`)를 받는다.
  - 일반 함수는 파라미터를 받아서 파라미터를 함수 내부에서 호출한다. 그렇기 때문에 파라미터에 대한 이름을 직접 넣어줄 수 있다.
- run은 확장 함수(`T.() -> R`)를 받는다.
  - T에 대한 확장 함수를 받는다. 확장함수는 본인 자신을 this로 호출하고, 생략할 수 있었다.

with(파라미터, 람다) : this를 사용해 접근하고, this는 생략 가능하다. 

```kotlin
val person = Person("유녕진, 100")
with(person) {
    println(this.name)
    println(name)
}
```

<img width="441" alt="image" src="https://github.com/yoon-youngjin/library-app/assets/83503188/de97f2fa-989d-4829-83de-61609f962efd">


### 3. 언제 어떤 scope function을 사용해야 할까?

#### let

1. 하나 이상의 함수를 call chain 결과로 호출할 때 

```kotlin
val strings = listOf("APPLE", "CAR")
strings.map { it.length }
  .filter { it > 3}
  .let { lengths -> println(lengths) }
//  .let(::println)
```
2. non-null 값에 대해서만 code block을 실행시킬 때 (이 경우가 가장 많이 사용)

```kotlin
val length = str?.let {
    println(it.uppercase())
    it.length
}
```

3. 일회성으로 제한된 영역에 지역 변수를 만들 때 (변수를 만드는 과정에서 추가적인 변수를 넣고 싶지 않을 때, depth가 커지므로 잘 사용하지 않음)
```kotlin
val numbers = listOf("one", "two", "three", "four")
val modifiedFirstItem = numbers.first()
  .let { firstItem ->
      if (firstItem.length >= 5) firstItem 
      else "!$firstItem"
  }.uppercase()
println(modifiedFirstItem)
```

#### run

1. 객체 초기화와 반환 값의 계산을 동시에 해야 할 때 

```kotlin
val person = Person("유녕진", 100).run(personRepository::save)
```

객체를 만들어 DB에 바로 저장하고, 해당 인스턴스를 다음 로직에 활용할 때 사용할 수 있다.
run을 통해 저장된 person이 반환된디.

```kotlin
val person = Person("유녕진", 100).run {
    hobby = "독서"
    personRepository.save(this)
}
```

응용하면 위와 같이 hobby 필드에 값을 할당하고, 저장할 수도 있다. 

하지만 이러한 코드를 개인적으로 잘 사용하지 않는다. 아래 코드가 익숙하기도 하고, 반복되는 생성 후처리(hobby에 값을 할당하는 등)는 생성자, 프로퍼티, init block으로 넣는 것이 좋다.

```kotlin
val person = personRepository.save(Person("유녕진", 100))
```

#### apply

> apply 특징 : 객체 그 자체가 반환된다.

1. 객체 설정을 할 때에 객체를 수정하는 로직이 call chain 중간에 필요할 때

Test Fixture를 만들 때 사용할 수 있다.

```kotlin
fun createPerson(
    name: String, 
    age: Int,
    hobby: String,
): Person {
    return Person(
        name = name,
        age = age,
    ).apply {
        this.hobby = hobby
    }
}
```
- 만약 hobby라는 필드가 어떤 convention에 따라서 혹은 모종의 룰에 따라서 constructor에 존재하지 않는 경우를 가정
  - Person이 최초 생성된 시점에는 name, age만 받다가 회원가입이 완료된 이후에 hobby를 추가적으로 입력하는 경우

```kotlin
val person = Person("유녕진", 100)
person.apply { this.growOld() }
  .let { println(it) }
```

위와 같은 코드도 이론상 가능하지만 잘 사용하지는 않는다.

#### also

> also 특징 : 객체 그 자체가 반환된다.

1. 객체 설정을 할 때에 객체를 수정하는 로직이 call chain 중간에 필요할 때

```kotlin
mutableListOf("one", "two", "three")
  .also { println("four 추가 이전 지금 값: $it") }
  .add("four")
```

잘 사용하지 않음.

#### with

특정 객체를 다른 객체로 변환해야 하는데, 모듈 간의 의존성에 의해 정적 팩토리 혹은 toClss 함수를 만들기 어려울 때 사용할 수 있다.

```kotlin
return with(person) {
    PersonDto(
        name = name,
        age = age,
    )
}
```

### 4. scope function의 가독성

scope function을 사용한 코드가 그렇지 않은 코드보다 가독성 좋은 코드일까? 

```kotlin
// 1번 코드
if (person != null && person.isAudlt) {
    view.showPerson(person)
} else {
    view.showError()
}

// 2번 코드
person?.takeIf { it.isAudult }
  ?.let(view::showPerson)
  ?: view.showError()
```

개인적으로 1번 코드가 훨씬 좋은 코드라 생각한다.

1. 구현 2는 숙력된 코틀린 개발자만 알아보기 쉽다. 어쩌면 숙련된 코틀린 개발자도 잘 이해하지 못할 수 있다.
2. 구현 1의 디버깅이 쉽다.
3. 구현 1이 수정도 더 쉽다.

또한, 2번 코드에 찾기 어려운 오류가 발생할 여지가 존재한다. 만약 `view.showPeson`이 null을 반환한다면, let은 람다의 결과를 반환하기 때문에, elvis 연산자에 의해서 `view.showError` 또한 호출되게 된다.

위와 같이 사용 빈도가 적은 관용구는 코드를 더 복잡하게 만들고 이런 관용구들을 한 문장 내에서 조합해 사용하면 복잡성이 훨씬 증가한다.
하지만 scope function을 사용하면 안되는 것도 아니다. 적절한 convention을 적용하면 유용하게 활용할 수 있다.
