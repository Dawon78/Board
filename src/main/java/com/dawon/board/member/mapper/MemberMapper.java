package com.dawon.board.member.mapper;

import com.dawon.board.member.domain.Member;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {
    void insert(Member member);
    Member findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByNickname(String nickname);
}
