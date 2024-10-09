# Spring log practice

## 1. Log?

> IT에서 발행되는 모든 행위와 이벤트 정보를 시간에 따라 남겨둔 데이터

* **로그를 통해서 애플리케이션의 상태를 관찰**할 수 있고, **오류가 발생한 부분에 대해서 인지**할 수 있다.
  * 로직의 흐름, 예외 등을 파악 가능
  * 로그를 통해서 서비스의 품질을 관리
* 로그를 남기는 것 또한 비용이 발생하기 때문에 적절한 상황, 적절한 로그를 작성하는 것이 중요하다.
  * 적절한 상황: 애플리케이션이 어떠한 행동을 하는지에 초점을 맞춘다면, 쉽게 찾을 수 있다.

### 1-1. 나쁜 로그

```java
try {
    //business logic
} catch (Exception e) {
    log.error("{}", e.getClass());
    //exception logic    
}
```

* 로직에서 예외가 발생했을 때 로그를 기록하는 것은 좋다.
* 하지만 위와 같이 단순히 예외가 발생했을 때 예외만 출력한다면 어떠한 예외가 왜 발생했는지 알 수 없다.
  * 코드의 양이 적고, 자신이 작성한 코드라면 알 수 있지만, 대부분의 상황이 그렇지 않기 때문에 로그는 **누가 봐도 의미를 파악할 수 있도록 해주는 것이 중요**하다.

### 1-2. 좋은 로그

```java
try {
    //business logic
} catch (Exception e) {
    log.error("userId={}, uri={}, method={}, parameter={}, time={}, errorMessage={}", 
              userId, uri, method, parameter, time, e.getMessage());
    //exception logic    
}
```

* 누가, 어떤 경로, 호출한 메서드, 파라미터, 언제, 발생한 이유 같이 상세하게 로그를 남긴다면, 로그의 의미를 쉽게 파악할 수 있다.

---

## 2. Springboot 로그 사용

* 과거에는 `log4j`를 사용해서 로그를 남겼다.
* 하지만 개발이 중단됐고, 현재는 성능이 더 좋은 `logback`을 사용한다.

### 2-1. logback?

> 자바의 오픈소스 로깅 프레임워크

* Slf4j의 구현체이다.
* Springboot는 `spring-boot-starter-logging`에 **기본적으로 내장**되어 있기 때문에 별도로 dependency를 추가하지 않아도 된다.

### 2-2. 로그 사용

#### 1. LoggerFactory 사용

```java
private static final Logger log = LoggerFactory.getLogger(Log.class);
```

#### 2. Lombok 사용

```java
@Slf4j
public class Log {
}
```

### 2-3. Springboot 로그 설정

1. resources 밑에 `logback-spring.xml`을 읽어 로그를 만든다.
2. `logback-spring.xml`이 없다면, `.yml` 혹은 `.properties` 파일을 읽어 로그를 만든다.
3. 만약 둘 다 존재하면 `.yml` 파일을 먼저 읽고, `logback-spring.xml` 파일에서 추가되는 부분을 사용한다.

* profile을 나눠 환경에 따라 다른 로그를 찍을 수 있다.

### 2-4. 로그 레벨

`TRACE` < `DEBUG` < `INFO` < `WARN` < `ERROR`

1. `ERROR`: 요청을 처리하는 중 오류가 발생한 경우 표시
2. `WARN`: 처리 가능한 문제, 향후 시스템 에러의 원인이 될 수 있는 경고성 메시지를 표시
3. `INFO`: 상태 변경과 같은 정보성 로그를 표시
4. `DEBUG`: 프로그램을 디버깅하기 위한 정보를 표시
5. `TRACE`: DEBUG보다 훨씬 상세한 정보를 표시

* 로그 레벨을 INFO로 설정하면 자신보다 높은 로그까지 찍는다. (INFO ~ ERROR)

### 2-5. 로그 범위 지정

* 전체 혹은 패키지 별로 로그를 남기도록 할 수 있다.

```yaml
logging:
  level:
    root: warn
    com.example.log: debug
```

## 3. application.yml 로그 적용

```yaml
logging:
  level: #적용할 곳과 레벨을 지정
    root: warn
    com.example.log: debug
  file: #로그 파일에 대한 명시, 반드시 name과 path중 하나만 사용해야 한다. (path 사용을 권장)
    name: #로그 파일의 이름
    path: #로그 파일의 위치 절대 경로
  logback:
    rollingpolicy: 
      file-name-pattern: #로그 파일의 이름을 설정하는 패턴. [파일명 + 날짜]와 같이 지정할 수 있음
      clean-history-on-start: #애플리케이션 재실행시 로그 파일 초기화 여부 설정
      max-file-size: #로그 파일의 최대 크기, 크기를 넘어서면 새로운 로그 파일 생성
      max-history: #로그 파일의 최대 갯수
      total-size-cap: #로그 파일의 총 크기, 넘어갈 시 가장 오래된 로그 파일 삭제
  pattern:
    console: #콘솔 창에 출력될 로그의 패턴
    level: #출력 로그 레벨 지정
    file: #로그 파일에 사용될 로그 패턴
    dateformat: #로그의 date에 대한 디폴트 설정
```