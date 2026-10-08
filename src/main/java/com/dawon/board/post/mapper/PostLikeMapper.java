package com.dawon.board.post.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PostLikeMapper {
    boolean exists(@Param("postId") Long postId, @Param("memberId") Long memberId);
    void insert(@Param("postId") Long postId, @Param("memberId") Long memberId);
    void delete(@Param("postId") Long postId, @Param("memberId") Long memberId);
    int countByPostId(Long postId);
    void deleteByPostId(Long postId);
}