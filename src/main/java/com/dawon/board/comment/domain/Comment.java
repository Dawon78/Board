package com.dawon.board.comment.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class Comment {
    private Long id;
    private Long postId;
    private Long memberId;
    private Long parentId;
    private String content;
    private boolean deleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}