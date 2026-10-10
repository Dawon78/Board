package com.dawon.board.comment.service;

import com.dawon.board.IntegrationTest;
import com.dawon.board.comment.dto.CommentForm;
import com.dawon.board.comment.mapper.CommentMapper;
import com.dawon.board.common.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@IntegrationTest
class CommentServiceTest {

    @Autowired
    CommentService commentService;

    @Autowired
    CommentMapper commentMapper;

    private CommentForm form(String content, Long parentId) {
        CommentForm form = new CommentForm();
        form.setContent(content);
        form.setParentId(parentId);
        return form;
    }

    @Test
    @DisplayName("답글에 답글을 달면 최상위 댓글 아래로 붙는다")
    void replyToReplyAttachesToTop() {
        Long top = commentService.create(2L, form("댓글", null), 1L);
        Long reply = commentService.create(2L, form("답글", top), 2L);
        Long replyOfReply = commentService.create(2L, form("답글의 답글", reply), 1L);

        assertThat(commentMapper.findById(replyOfReply).getParentId()).isEqualTo(top);
    }

    @Test
    @DisplayName("다른 글의 댓글에는 답글을 달 수 없다")
    void cannotReplyToOtherPostsComment() {
        Long otherPostComment = commentService.create(1L, form("1번 글 댓글", null), 1L);

        assertThatThrownBy(() -> commentService.create(2L, form("조작된 답글", otherPostComment), 1L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("답글이 있는 댓글을 지우면 삭제 표시만 남는다")
    void softDeleteWhenHasReplies() {
        Long top = commentService.create(2L, form("댓글", null), 1L);
        commentService.create(2L, form("답글", top), 2L);

        commentService.delete(top, 1L);

        assertThat(commentMapper.findById(top).isDeleted()).isTrue();
    }

    @Test
    @DisplayName("삭제 표시된 댓글의 마지막 답글을 지우면 부모도 함께 지워진다")
    void cleanUpParentAfterLastReply() {
        Long top = commentService.create(2L, form("댓글", null), 1L);
        Long reply = commentService.create(2L, form("답글", top), 2L);
        commentService.delete(top, 1L);

        commentService.delete(reply, 2L);

        assertThat(commentMapper.findById(reply)).isNull();
        assertThat(commentMapper.findById(top)).isNull();
    }

    @Test
    @DisplayName("작성자가 아니면 댓글을 지울 수 없다")
    void cannotDeleteOthersComment() {
        Long top = commentService.create(2L, form("댓글", null), 1L);

        assertThatThrownBy(() -> commentService.delete(top, 2L))
                .isInstanceOf(AccessDeniedException.class);
    }
}