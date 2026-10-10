package com.dawon.board.comment.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CommentLikeMapper {
    boolean exists(@Param("commentId") Long commentId, @Param("memberId") Long memberId);
    void insert(@Param("commentId") Long commentId, @Param("memberId") Long memberId);
    void delete(@Param("commentId") Long commentId, @Param("memberId") Long memberId);
    int countByCommentId(Long commentId);
    void deleteByCommentId(Long commentId);
    void deleteByPostId(Long postId);
}