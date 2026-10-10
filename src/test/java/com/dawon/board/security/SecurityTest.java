package com.dawon.board.security;

import com.dawon.board.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
class SecurityTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("로그인하지 않아도 목록과 상세는 볼 수 있다")
    void anonymousCanRead() throws Exception {
        mockMvc.perform(get("/posts")).andExpect(status().isOk());
        mockMvc.perform(get("/posts/1")).andExpect(status().isOk());
    }

    @Test
    @DisplayName("로그인하지 않으면 글쓰기 화면 대신 로그인 화면으로 이동한다")
    void anonymousCannotWrite() throws Exception {
        mockMvc.perform(get("/posts/new"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    @WithUserDetails("test1")
    @DisplayName("CSRF 토큰 없이 글을 저장하면 403")
    void postWithoutCsrfIsForbidden() throws Exception {
        mockMvc.perform(post("/posts").param("title", "제목").param("content", "내용"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("test1")
    @DisplayName("남의 글 수정 화면에 들어가면 403")
    void cannotEditOthersPost() throws Exception {
        mockMvc.perform(get("/posts/1/edit"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("test1")
    @DisplayName("남의 글 삭제 요청은 403이고 글은 그대로 남는다")
    void cannotDeleteOthersPost() throws Exception {
        mockMvc.perform(post("/posts/1/delete").with(csrf()))
                .andExpect(status().isForbidden());
        mockMvc.perform(get("/posts/1")).andExpect(status().isOk());
    }
}