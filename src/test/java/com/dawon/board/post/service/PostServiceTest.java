package com.dawon.board.post.service;

import com.dawon.board.IntegrationTest;
import com.dawon.board.comment.dto.CommentForm;
import com.dawon.board.comment.service.CommentLikeService;
import com.dawon.board.comment.service.CommentService;
import com.dawon.board.common.NotFoundException;
import com.dawon.board.post.dto.PostForm;
import com.dawon.board.post.dto.PostView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@IntegrationTest
class PostServiceTest {

    @Autowired
    PostService postService;

    @Autowired
    PostLikeService postLikeService;

    @Autowired
    CommentService commentService;

    @Autowired
    CommentLikeService commentLikeService;

    private PostForm postForm(String title, String content) {
        PostForm form = new PostForm();
        form.setTitle(title);
        form.setContent(content);
        return form;
    }

    private CommentForm commentForm(String content, Long parentId) {
        CommentForm form = new CommentForm();
        form.setContent(content);
        form.setParentId(parentId);
        return form;
    }

    @Test
    @DisplayName("작성자가 아니면 글을 수정할 수 없다")
    void cannotUpdateOthersPost() {
        assertThatThrownBy(() -> postService.update(1L, postForm("수정", "내용"), 1L))
                .isInstanceOf(AccessDeniedException.class);
    }

    @Test
    @DisplayName("작성자는 글을 수정할 수 있고 수정됨으로 표시된다")
    void updateOwnPost() {
        postService.update(2L, postForm("바뀐 제목", "바뀐 내용"), 1L);

        PostView post = postService.findDetail(2L);
        assertThat(post.getTitle()).isEqualTo("바뀐 제목");
        assertThat(post.isEdited()).isTrue();
    }

    @Test
    @DisplayName("없는 글을 조회하면 예외가 발생한다")
    void notFound() {
        assertThatThrownBy(() -> postService.findDetail(9999L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("좋아요, 댓글, 답글, 댓글 좋아요가 달린 글도 한 번에 삭제된다")
    void deleteWithChildren() {
        postLikeService.toggle(2L, 2L);
        Long parentId = commentService.create(2L, commentForm("댓글", null), 2L);
        Long replyId = commentService.create(2L, commentForm("답글", parentId), 1L);
        commentLikeService.toggle(parentId, 1L);
        commentLikeService.toggle(replyId, 2L);

        postService.delete(2L, 1L);

        assertThatThrownBy(() -> postService.findDetail(2L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("검색은 대소문자를 구분하지 않는다")
    void searchIgnoresCase() {
        Long id = postService.create(postForm("Hello Spring", "내용"), 1L);

        assertThat(postService.findPage(1, "title", "hello")).extracting(PostView::getId).contains(id);
        assertThat(postService.findPage(1, "title", "SPRING")).extracting(PostView::getId).contains(id);
    }

    @Test
    @DisplayName("검색어의 %는 모든 글이 아니라 진짜 % 글자로 찾는다")
    void searchEscapesWildcard() {
        Long id = postService.create(postForm("할인 50% 이벤트", "내용"), 1L);

        List<PostView> result = postService.findPage(1, "title", "%");

        assertThat(result).extracting(PostView::getId).containsExactly(id);
    }
}