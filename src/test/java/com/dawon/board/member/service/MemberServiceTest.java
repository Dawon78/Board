package com.dawon.board.member.service;

import com.dawon.board.IntegrationTest;
import com.dawon.board.member.domain.Member;
import com.dawon.board.member.dto.SignupForm;
import com.dawon.board.member.mapper.MemberMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@IntegrationTest
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberMapper memberMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    private SignupForm form(String username, String nickname) {
        SignupForm form = new SignupForm();
        form.setUsername(username);
        form.setPassword("test1234!");
        form.setPasswordConfirm("test1234!");
        form.setNickname(nickname);
        return form;
    }

    @Test
    @DisplayName("회원가입하면 비밀번호가 BCrypt로 암호화되어 저장된다")
    void signupEncodesPassword() {
        memberService.signup(form("newuser1", "새회원"));

        Member saved = memberMapper.findByUsername("newuser1");
        assertThat(saved.getPassword()).isNotEqualTo("test1234!");
        assertThat(passwordEncoder.matches("test1234!", saved.getPassword())).isTrue();
    }

    @Test
    @DisplayName("이미 있는 아이디로 가입하면 예외가 발생한다")
    void duplicateUsername() {
        assertThatThrownBy(() -> memberService.signup(form("test1", "다른닉네임")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("아이디");
    }

    @Test
    @DisplayName("대소문자만 다른 닉네임은 중복으로 본다")
    void duplicateNicknameIgnoringCase() {
        memberService.signup(form("abcuser1", "Abc"));

        assertThatThrownBy(() -> memberService.signup(form("abcuser2", "abc")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("닉네임");
    }

    @Test
    @DisplayName("비밀번호 확인이 다르면 예외가 발생한다")
    void passwordMismatch() {
        SignupForm form = form("newuser2", "새회원이");
        form.setPasswordConfirm("different1!");

        assertThatThrownBy(() -> memberService.signup(form))
                .isInstanceOf(IllegalArgumentException.class);
    }
}