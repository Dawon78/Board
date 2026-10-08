package com.dawon.board.security;

import com.dawon.board.member.domain.Member;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
public class LoginMember implements UserDetails {

    private final Long id;
    private final String username;
    private final String password;
    private final String nickname;

    public LoginMember(Member member) {
        this.id = member.getId();
        this.username = member.getUsername();
        this.password = member.getPassword();
        this.nickname = member.getNickname();

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));   // 권한: 일반 회원
    }

}
