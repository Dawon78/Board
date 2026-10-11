package com.dawon.board.member.service;

import com.dawon.board.member.domain.Member;
import com.dawon.board.member.dto.SignupForm;
import com.dawon.board.member.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long signup(SignupForm form) {

        //비밀번호 확인용
        if (!form.getPassword().equals(form.getPasswordConfirm())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다");
        }

        //아이디 중복확인
        if (memberMapper.existsByUsername(form.getUsername())) {
            throw new IllegalArgumentException("이미 사용중인 아이디입니다.");
        }

        //닉네임 중복확인
        if (memberMapper.existsByNickname((form.getNickname()))) {
            throw new IllegalArgumentException("이미 사용중인 닉네임입니다");
        }

        Member member = new Member();
        member.setUsername(form.getUsername());
        member.setNickname(form.getNickname());
        member.setPassword(passwordEncoder.encode(form.getPassword()));

        try {
            memberMapper.insert(member);
        } catch (DuplicateKeyException e) {
            throw new IllegalArgumentException("이미 사용중인 아이디 또는 닉네임입니다.");

        }
        return member.getId();
    }

}
