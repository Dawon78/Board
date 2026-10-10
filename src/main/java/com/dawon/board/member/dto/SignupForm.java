package com.dawon.board.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupForm {

    @NotBlank(message = "아이디를 입력해 주세요.")
    @Pattern(regexp = "^[a-z0-9]{4,20}$", message = "아이디는 영문 소문자와 숫자 4~20자로 입력해 주세요.")
    private String username;

    @NotBlank(message = "비밀번호를 입력해 주세요.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[^A-Za-z0-9])\\S{8,20}$",
            message = "비밀번호는 영문, 숫자, 특수문자를 포함해 8~20자로 입력해 주세요.")
    private String password;

    @NotBlank(message = "비밀번호 확인을 입력해 주세요.")
    private String passwordConfirm;

    @NotBlank(message = "닉네임을 입력해 주세요.")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,20}$", message = "닉네임은 한글, 영문, 숫자 2~20자로 입력해 주세요.")
    private String nickname;
}
