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

## 4. logback-spring.xml 로그 적용

### 4-1. 기본 특징

* 대소문자를 구분하지 않는다.
* name attribute를 반드시 지정해야 한다.
* logback-spring.xml은 appender와 logger 크게 두 개로 구분한다.
* dynamic reloading 기능을 지원한다.

### 4-2. appender

> 로그의 형태를 설정, 로그 메시지가 출력될 대상을 결정하는 요소

#### appender의 클래스 종류

1. `ch.qos.logback.core.ConsoleAppender`: **콘솔**에 로그를 출력, 로그를 OutputStream에 작성하여 콘솔에 출력
2. `ch.qos.logback.core.FileAppender`: **파일**에 로그를 출력, 최대 보관일수를 지정할 수 있음
3. `ch.qos.logback.core.rolling.RollingFileAppender`: **여러개의 파일**을 롤링, 순회하면서 로그를 출력
4. `ch.qos.logback.classic.net.SMTPAppender`: 로그를 **메일**로 전송
5. `ch.qos.logback.classic.db.DBAppender`: **데이터베이스**에 로그를 저장

### 4-3. root, logger

> 설정한 appender를 참조하여 package와 level 설정

1. root
   * 전역 설정
   * 지역적으로 선언된 logger 설정이 있다면 해당 logger 설정이 default로 적용
2. logger
   * 지역 설정
   * additivity 값은 root 설정 상속 유무 설정 (default = true)

### 4-4. property

> 설정 파일에서 사용될 변수값 선언

### 4-5. layout, encoder

> 로그 출력 포맷 지정

* Layout: 로그의 출력 포맷을 지정
* encoder: Appender에 포함되어 사용자가 지정한 형식으로 표현될 로그메시지를 변환하는 역활을 담당
  * 바이트를 소유하고 있는 Appender가 관리하는 OutputStream에 쓸 시간과 내용을 제어할 수 있음
  * FileAppender와 하위 클래스는 encoder를 필요로 하고 더 이상 layout은 사용하지 않음

### 4-6. pattern

* `%Logger{length}`: Logger name을 축약할 수 있다. {length}는 최대 자리 수 `logger{35}`
* `%-5level`: 로그 레벨, -5는 출력의 고정폭 값(5글자)
* `%msg`, `%message`: 로그 메시지
* `${PID:-}`: 프로세스 아이디
* `%d`: 로그 기록시간
* `%p`: 로깅 레벨
* `%F`: 로깅이 발생한 프로그램 파일명
* `%M`: 로깅이 발생한 메서드명
* `%I`: 로깅이 발생한 호출지 정보
* `%L`: 로깅이 발생한 호출지 라인 수
* `%thread`: 현재 스레드명
* `%t`: 로깅이 발생한 스레드명
* `%c`: 로깅이 발생한 카테고리
* `%C`: 로깅이 발생한 클래스명
* `%m`: 로그 메시지
* `%n`: 줄바꿈
* `%%:` %출력
* `%r`: 애플리케이션 시작 이후부터 로깅이 발생한 시점까지의 시간(ms)

### 4-7. 기타

* `<file>`
  * 기록할 파일명과 경로를 설정
* `<rollingPolicy class>`
  * `ch.qos.logback.core.rolling.TimeBaseRollingPolicy`: 일자별 적용
  * `ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP`: 일자 + 크기별 적용 (~ Springboot 3.3.3)
  * `ch.qos.logback.core.rolling.SizeAndTimeBasedFileNamingAndTriggeringPolicy`: 일자 + 크기별 적용 (Springboot 3.3.4 ~)
* `<fileNamePattern>`
  * 파일 쓰기가 종료된 로그 파일명의 패턴을 지정
  * `.gz`나 `.zip`으로 자동으로 압축할 수 있음
* `<maxFileSize>`
  * 한 파일당 최대 파일 용량을 지정
  * 로그 내용의 크기도 IO 성능에 영향을 미치기 때문에 되도록이면 너무 크지 않은 사이즈로 지정하는 것이 좋음 (최대 10MB 내외 권장)
  * 용량의 단위는 KB, MB, GB 3가지를 지정할 수 있음
  * RollingFile 이름 패턴에 `.gz`나 `.zip`을 입력한 경우 로그 파일을 자동으로 압축해주는 기능도 있음
* `<maxHistory>`
  * 최대 파일 생성 갯수
* `<Filter>`
  * 해당 패키지에 무조건 로그를 찍는 것 말고도 필터링이 필요한 경우에 사용