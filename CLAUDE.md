 Claude 작업 지침

 1. 프로젝트
- 게시판 서비스 구현 과제 (요구사항: README.md)
- 스택: Spring Boot 4.1 / MyBatis / JSP + JSTL + jQuery / H2 / Spring Security
- 마감: 2026-10-14 원본 저장소에 PR을 올려 제출한다.

 2. 역할 분담
- 코드와 SQL은 내가 직접 작성한다.
- 설정 파일(build.gradle, application.properties)은 요청할 때만 Claude가 수정하고, 무엇을 바꿨는지 알려준다.
- 실행과 테스트로 확인하는 것은 내가 직접 한다. Claude는 확인 방법만 알려준다.

 3. 답하는 방식
- 요구사항을 먼저 준다. 정답 코드는 처음 다루는 개념일 때만 준다.
- 리뷰는 문제가 있는 곳만 짚고, 고친 코드는 주지 않는다.
- 작은 단위로 진행하고, 한 단위가 끝나면 커밋하라고 알려준다.
- 내가 정답 코드를 직접 요청하면 준다.

 4. 기술 기준
- Spring Boot 4.1 / Spring Security 7 기준으로 답한다.
- DB 이름은 소문자 snake_case(예: post_like, created_at)로, SQL 키워드는 대문자로 쓴다.

 5. 금지
- git 커밋과 푸시를 하지 않는다.
- PR을 Merge하지 않는다.
- API 키나 개인정보를 출력하지 않는다.