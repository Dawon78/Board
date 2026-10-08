INSERT INTO member(username, password, nickname)
VALUES ('test1', '$2a$10$llBqtyGE68DdAoE1ZW9LeuIVmnmjEAqxiwlAbFzu1v.42fZAqzQX6', '테스트1');

INSERT INTO member(username, password, nickname)
VALUES ('test2', '$2a$10$llBqtyGE68DdAoE1ZW9LeuIVmnmjEAqxiwlAbFzu1v.42fZAqzQX6', '테스트2');

INSERT INTO post (member_id, title, content)
SELECT CASE WHEN X % 2 = 0 THEN 1 ELSE 2 END,
       '테스트 게시글 ' || X,
       '테스트 게시글 ' || X || '번의 내용입니다.'
FROM SYSTEM_RANGE(1, 30);


