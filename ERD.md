# ERD 설계

## 1. 개요
- 테이블 5개: `member`, `post`, `comment`, `post_like`, `comment_like`
- 이름 규칙: 소문자 snake_case. 모든 테이블의 PK는 `id` (BIGINT, 자동 증가)

## 2. 테이블 정의서

### member (회원)
| 논리명 | 물리명 | 타입 | NULL | 키 | 기본값 |
|---|---|---|---|---|---|
| 회원번호 | id | BIGINT | N | PK | 자동 증가 |
| 아이디 | username | VARCHAR(20) | N | UK | |
| 비밀번호 | password | VARCHAR(100) | N | | |
| 닉네임 | nickname | VARCHAR(20) | N | UK | |
| 가입일시 | created_at | TIMESTAMP | N | | CURRENT_TIMESTAMP |

### post (게시글)
| 논리명 | 물리명 | 타입 | NULL | 키 | 기본값 |
|---|---|---|---|---|---|
| 게시글번호 | id | BIGINT | N | PK | 자동 증가 |
| 회원번호 | member_id | BIGINT | N | FK → member | |
| 제목 | title | VARCHAR(100) | N | | |
| 내용 | content | VARCHAR(4000) | N | | |
| 작성일시 | created_at | TIMESTAMP | N | | CURRENT_TIMESTAMP |
| 수정일시 | updated_at | TIMESTAMP | Y | | |

### comment (댓글)
| 논리명 | 물리명 | 타입 | NULL | 키 | 기본값 |
|---|---|---|---|---|---|
| 댓글번호 | id | BIGINT | N | PK | 자동 증가 |
| 게시글번호 | post_id | BIGINT | N | FK → post | |
| 회원번호 | member_id | BIGINT | N | FK → member | |
| 부모댓글번호 | parent_id | BIGINT | Y | FK → comment | |
| 댓글내용 | content | VARCHAR(500) | N | | |
| 삭제여부 | deleted | BOOLEAN | N | | FALSE |
| 작성일시 | created_at | TIMESTAMP | N | | CURRENT_TIMESTAMP |
| 수정일시 | updated_at | TIMESTAMP | Y | | |

### post_like (게시글 좋아요)
| 논리명 | 물리명 | 타입 | NULL | 키 | 기본값 |
|---|---|---|---|---|---|
| 좋아요번호 | id | BIGINT | N | PK | 자동 증가 |
| 게시글번호 | post_id | BIGINT | N | FK → post, UK | |
| 회원번호 | member_id | BIGINT | N | FK → member, UK | |
| 누른일시 | created_at | TIMESTAMP | N | | CURRENT_TIMESTAMP |

### comment_like (댓글 좋아요)
| 논리명 | 물리명 | 타입 | NULL | 키 | 기본값 |
|---|---|---|---|---|---|
| 좋아요번호 | id | BIGINT | N | PK | 자동 증가 |
| 댓글번호 | comment_id | BIGINT | N | FK → comment, UK | |
| 회원번호 | member_id | BIGINT | N | FK → member, UK | |
| 누른일시 | created_at | TIMESTAMP | N | | CURRENT_TIMESTAMP |

## 3. 설계 결정과 이유

1. 대댓글은 1단계만, parent_id는 항상 최상위 댓글을 가리킵니다
```
댓글 A
├─ 대댓글 B
└─ 대댓글 C
```
- 댓글을 여러 단계로 나누지 않고 같은 댓글 아래에서 답글을 관리하기 위해 이렇게 정했습니다
답글에 다시 답글을 달아도 최상위 댓글을 부모로 두고 @닉네임으로 표시하도록 했습니다.

2. 좋아요는 별도 테이블 + 복합 UNIQUE를 사용합니다
- 중복 좋아요를 DB에서 방지하기 위해서입니다.

3. 좋아요 수는 COUNT로 계산합니다
- 동시성으로 카운터 값이 실제 좋아요 수와 달라지는 문제를 피하기 위해서입니다.

4. 댓글은 답글이 있을 때만 소프트 삭제합니다
- 다른 사람이 단 답글이 함께 사라지지 않게 하기 위해서입니다. 부모 댓글은 "삭제된 댓글입니다"로만 표시하고, 답글이 없으면 실제로 삭제합니다.

5. ON DELETE CASCADE를 사용하지 않습니다
- 삭제 순서를 직접 관리하고 트랜잭션으로 처리하기 위해서입니다.

6. updated_at이 NULL이면 수정하지 않은 글로 봅니다
- 글 수정 여부를 구분하기 위해서입니다.

7. 테이블 이름은 member, post_like로 정합니다
- `user`, `like`가 H2 예약어라서 충돌을 피하기 위해서입니다.

8. id는 BIGINT를 사용합니다
- 데이터가 많아져도 충분한 범위를 확보하기 위해서입니다.

9. role 컬럼은 두지 않습니다
- 현재 관리자 기능이 없어 권한 구분이 필요하지 않습니다.
