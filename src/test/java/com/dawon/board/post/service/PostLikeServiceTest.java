package com.dawon.board.post.service;

import com.dawon.board.IntegrationTest;
import com.dawon.board.common.NotFoundException;
import com.dawon.board.post.dto.LikeResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@IntegrationTest
class PostLikeServiceTest {

    @Autowired
    PostLikeService postLikeService;

    @Test
    @DisplayName("처음 누르면 좋아요, 다시 누르면 취소된다")
    void toggle() {
        LikeResponse first = postLikeService.toggle(2L, 1L);
        assertThat(first.liked()).isTrue();
        assertThat(first.count()).isEqualTo(1);

        LikeResponse second = postLikeService.toggle(2L, 1L);
        assertThat(second.liked()).isFalse();
        assertThat(second.count()).isZero();
    }

    @Test
    @DisplayName("회원마다 좋아요가 따로 세어진다")
    void countsEachMember() {
        postLikeService.toggle(2L, 1L);
        LikeResponse result = postLikeService.toggle(2L, 2L);

        assertThat(result.count()).isEqualTo(2);
    }

    @Test
    @DisplayName("없는 글에 좋아요를 누르면 예외가 발생한다")
    void notFound() {
        assertThatThrownBy(() -> postLikeService.toggle(9999L, 1L))
                .isInstanceOf(NotFoundException.class);
    }
}