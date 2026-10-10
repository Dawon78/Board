package com.dawon.board.comment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
public class CommentView {

    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private Long id;
    private Long postId;
    private Long memberId;
    private Long parentId;
    private String nickname;
    private String content;
    private boolean deleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int likeCount;
    private boolean liked;

    public String getCreatedAtText() {
        return createdAt == null ? "" : createdAt.format(FORMAT);
    }

    public boolean isEdited() {
        return updatedAt != null;
    }

    public boolean isReply() {
        return parentId != null;
    }
}