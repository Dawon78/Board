# 게시판 서비스

회원가입·로그인, 게시글, 댓글·대댓글, 좋아요, 검색 기능을 갖춘 게시판입니다.
Spring MVC + MyBatis + JSP 구조로, 화면은 서버에서 렌더링하고 좋아요만 jQuery Ajax로 처리합니다.

| 문서 | 내용 |
|---|---|
| [ERD.md](ERD.md) | ERD, 테이블 정의서, 설계 결정과 이유 |
| [AI_USAGE.md](AI_USAGE.md) | AI에게 맡긴 작업과 직접 한 작업, 고친 사례, 실제 질문 |
| [CLAUDE.md](CLAUDE.md) | AI(Claude Code)에게 준 작업 지침 |

## 1. 기술 스택

| 구분 | 사용 기술 |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 4.1.1, Spring MVC, Spring Security 7 |
| Persistence | MyBatis (mybatis-spring-boot-starter 4.1.0) |
| DB | H2 (in-memory) |
| View | JSP + JSTL, jQuery 3.7, Bootstrap 5.3 (CDN) |
| Build | Gradle (war) |
| Test | JUnit 5, AssertJ, MockMvc, Spring Security Test |


## 2. 실행 방법

**필요 환경:** JDK 17

```bash
./gradlew bootRun
```
Windows에서는 `gradlew.bat bootRun`도 가능

| 항목 | 값 |
|---|---|
| 접속 주소 | http://localhost:8087 |
| H2 콘솔 | http://localhost:8087/h2-console |
| H2 접속 정보 | JDBC URL `jdbc:h2:mem:board` / User `sa` / Password 없음 |
| 테스트 실행 | `./gradlew test` |

### 테스트 계정
앱을 실행할 때 `data.sql`로 자동 생성됩니다.

| 아이디 | 비밀번호 | 닉네임 |
|---|---|---|
| `test1` | `test1234!` | 테스트1 |
| `test2` | `test1234!` | 테스트2 |

- 게시글 30개도 함께 생성됨 (짝수 번호 글은 test1, 홀수 번호 글은 test2가 작성)
- 두 계정으로 번갈아 로그인하면 페이징과 작성자 권한을 바로 확인할 수 있음

## 3. 기능

### 필수 기능
| 기능 | 내용 |
|---|---|
| 회원 | 회원가입, 로그인, 로그아웃 (Spring Security 폼 로그인, BCrypt 암호화) |
| 게시글 | 작성·조회·수정·삭제, 페이징(10개씩), 작성자만 수정·삭제 |
| 댓글 | 작성·수정·삭제, 대댓글(1단계), 작성자만 수정·삭제 |
| 좋아요 | 누르면 좋아요, 다시 누르면 취소 (중복 불가) |
| 권한 | 로그인한 회원만 작성·댓글·좋아요, 목록과 상세는 누구나 조회 |

### 추가 기능
- 댓글 좋아요
- 검색: 제목 / 내용 / 작성자, 대소문자 구분 없음
- 답글이 있는 댓글은 "삭제된 댓글입니다"로 남기는 소프트 삭제
- 목록 → 상세 → 목록으로 돌아올 때 검색어와 페이지 유지
- 에러 화면: 403, 404, 그 외 4xx, 5xx
- 입력 규칙
  - 아이디: 영문 소문자 + 숫자 4~20자
  - 비밀번호: 영문 + 숫자 + 특수문자 포함 8~20자
  - 닉네임: 한글 / 영문 / 숫자 2~20자, 대소문자만 다른 닉네임은 중복으로 처리

## 4. 화면 / URL

- 데이터를 바꾸는 요청은 모두 POST (HTML 폼은 GET/POST만 지원, CSRF 보호 대상)
- 등록·수정·삭제 후에는 redirect로 이동해 새로고침 시 중복 제출 방지 (PRG 패턴)

| URL | Method | 설명 | 권한 |
|---|---|---|---|
| `/` | GET | 목록으로 이동 | 누구나 |
| `/signup` | GET / POST | 회원가입 화면 / 처리 | 비로그인 |
| `/login` | GET / POST | 로그인 화면 / 처리 (Spring Security) | 비로그인 |
| `/logout` | POST | 로그아웃 | 로그인 |
| `/posts?page=&type=&keyword=` | GET | 목록, 페이징, 검색 | 누구나 |
| `/posts/{id}` | GET | 상세 (댓글, 좋아요 포함) | 누구나 |
| `/posts/new` | GET | 글쓰기 화면 | 로그인 |
| `/posts` | POST | 글 등록 | 로그인 |
| `/posts/{id}/edit` | GET / POST | 수정 화면 / 처리 | 작성자 |
| `/posts/{id}/delete` | POST | 삭제 | 작성자 |
| `/posts/{id}/like` | POST (Ajax) | 게시글 좋아요 토글 | 로그인 |
| `/posts/{postId}/comments` | POST | 댓글·답글 등록 | 로그인 |
| `/posts/{postId}/comments/{id}/edit` | POST | 댓글 수정 | 작성자 |
| `/posts/{postId}/comments/{id}/delete` | POST | 댓글 삭제 | 작성자 |
| `/comments/{id}/like` | POST (Ajax) | 댓글 좋아요 토글 | 로그인 |

## 5. ERD

![ERD](EXERD.png)

테이블 정의서와 설계 결정은 [ERD.md](ERD.md)에 정리했습니다.

## 6. 구조

```
com.dawon.board
├─ config      SecurityConfig
├─ security    LoginMember, LoginMemberService (UserDetailsService)
├─ common      공통 예외 처리, 홈
├─ member      controller / service / mapper / domain / dto
├─ post        controller / service / mapper / domain / dto
└─ comment     controller / service / mapper / domain / dto

src/main/resources/mapper        MyBatis XML (SQL)
src/main/resources/schema.sql    테이블 생성
src/main/resources/data.sql      초기 데이터
src/main/webapp/WEB-INF/views    JSP
```

## 7. 보안

| 위협 | 대응 | 확인 |
|---|---|---|
| CSRF | Spring Security CSRF 사용. 폼은 hidden 토큰, Ajax는 요청 헤더에 토큰. 상태 변경은 POST만 | 토큰 없는 POST → 403 (테스트) |
| XSS | 사용자 입력은 `<c:out>`, `htmlEscape`로 출력. jQuery는 `.text()` 사용. 검색어는 `<c:url>`로 인코딩 | 제목에 `<script>` 입력 → 글자 그대로 표시 |
| SQL Injection | MyBatis `#{}` 바인딩만 사용 (`${}` 미사용). 검색 종류는 `<choose>`로 분기 | `' OR '1'='1` 검색 → 결과 0건 |
| 권한 우회 | 화면에서 버튼만 숨기지 않고 Service에서 작성자 확인 → 403 | 남의 글 수정·삭제 → 403 (테스트) |
| 비밀번호 | BCrypt 저장, 비밀번호 정책 정규식 | 저장값이 해시인지 확인 (테스트) |
| 계정 정보 노출 | 로그인 실패 시 아이디 존재 여부를 알 수 없는 공통 메시지 | |
| 세션 | 30분 타임아웃, 로그인 시 세션 ID 재발급, HttpOnly 쿠키 | |
| 보안 헤더 | X-Frame-Options(SAMEORIGIN), X-Content-Type-Options, Cache-Control(no-store). JSP 응답에도 적용되도록 헤더를 먼저 쓰게 설정 | 응답 헤더 확인 |
| 중복 요청 | 좋아요 테이블 복합 UNIQUE + 중복 예외 처리, 폼 제출 시 버튼 잠금 | 첫 제출 후 버튼이 잠기고 추가 클릭은 제출되지 않음 |
| 검색 특수문자 | LIKE의 `%`, `_`를 escape | `%` 검색 시 전체 노출 안 됨 (테스트) |

## 8. 테스트

| 테스트 | 검증 내용 |
|---|---|
| MemberServiceTest | BCrypt 저장, 아이디 중복, 대소문자만 다른 닉네임 중복, 비밀번호 확인 불일치 |
| SecurityTest | 비로그인 조회 허용, 비로그인 글쓰기 차단, CSRF 없는 요청 403, 남의 글 수정·삭제 403 |
| PostServiceTest | 작성자 아닌 수정 차단, 수정됨 표시, 없는 글, 좋아요·댓글·댓글 좋아요가 달린 글 삭제, 대소문자 무시 검색, `%` escape |
| PostLikeServiceTest | 좋아요 토글, 회원별 집계, 없는 글 |
| CommentServiceTest | 답글의 답글은 최상위 댓글 아래로, 다른 글 댓글에 답글 차단, 소프트 삭제, 마지막 답글 삭제 시 부모 정리, 작성자 아닌 삭제 차단 |

### 완성 후 점검

| 문제 | 원인 | 해결 |
|---|---|---|
| 영어 검색이 대소문자를 구분 (`hello`로 `Hello` 검색 안 됨) | H2의 LIKE는 대소문자 구분 | 컬럼과 검색어 양쪽에 `LOWER` |
| `%`, `_`로 검색하면 모든 글이 나옴 | LIKE 와일드카드로 해석됨 | 검색어 escape + `ESCAPE '\'` |
| 저장 버튼을 빠르게 여러 번 누르면 글이 여러 개 생성 | PRG는 새로고침 중복만 막음 | 제출 시 버튼 잠금 |
| `test1`과 `Test1`이 다른 계정으로 가입, 닉네임 끝 공백으로 사칭 가능 | 입력 규칙 없음 | 아이디·닉네임 정규식, 닉네임 대소문자 무시 중복 검사 |
| 주소를 직접 입력하면 Whitelabel 에러 화면 | 405 등에 맞는 에러 화면 없음 | `error/4xx.jsp`, `error/5xx.jsp` 추가 |
| 띄어쓰기 없는 긴 글이 화면을 가로로 넘침 | 줄바꿈 기준 없음 | `text-break` 적용 |
| 다른 탭에서 로그아웃 후 좋아요를 누르면 일반 오류 메시지 | 세션 만료로 403 응답 | 로그인 안내 후 로그인 화면으로 이동 |
| JSP 화면에 보안 헤더 누락 | JSP로 forward될 때 응답이 먼저 확정되어 헤더가 빠짐 | 보안 헤더를 먼저 쓰도록 설정 |
