CREATE TABLE member
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    username   VARCHAR(20)                         NOT NULL,
    password   VARCHAR(100)                        NOT NULL,
    nickname   VARCHAR(20)                         NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT uk_member_username UNIQUE (username),
    CONSTRAINT uk_member_nickname UNIQUE (nickname)
);

CREATE TABLE post
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id  BIGINT                              NOT NULL,
    title      VARCHAR(100)                        NOT NULL,
    content    VARCHAR(4000)                       NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    CONSTRAINT fk_post_member FOREIGN KEY (member_id) REFERENCES member (id)
);

CREATE TABLE comment
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    post_id    BIGINT                              NOT NULL,
    member_id  BIGINT                              NOT NULL,
    parent_id  BIGINT,
    content    VARCHAR(500)                        NOT NULL,
    deleted    BOOLEAN   DEFAULT FALSE             NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    CONSTRAINT fk_comment_post FOREIGN KEY (post_id) REFERENCES post (id),
    CONSTRAINT fk_comment_member FOREIGN KEY (member_id) REFERENCES member (id),
    CONSTRAINT fk_comment_parent FOREIGN KEY (parent_id) REFERENCES comment (id)
);

CREATE TABLE post_like
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    post_id    BIGINT                              NOT NULL,
    member_id  BIGINT                              NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT uk_post_like UNIQUE (post_id, member_id),
    CONSTRAINT fk_post_like_post FOREIGN KEY (post_id) REFERENCES post (id),
    CONSTRAINT fk_post_like_member FOREIGN KEY (member_id) REFERENCES member (id)
);

CREATE TABLE comment_like
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    comment_id BIGINT                              NOT NULL,
    member_id  BIGINT                              NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT uk_comment_like UNIQUE (comment_id, member_id),
    CONSTRAINT fk_comment_like_comment FOREIGN KEY (comment_id) REFERENCES comment (id),
    CONSTRAINT fk_comment_like_member FOREIGN KEY (member_id) REFERENCES member (id)
);