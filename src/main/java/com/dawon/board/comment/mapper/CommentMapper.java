package com.dawon.board.comment.mapper;

import com.dawon.board.comment.domain.Comment;
import com.dawon.board.comment.dto.CommentView;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {
    void insert(Comment comment);
    Comment findById(Long id);
    List<CommentView> findByPostId(Long postId);
    void update(Comment comment);
    void softDelete(Long id);
    void delete(Long id);
    int countReplies(Long parentId);
    void deleteRepliesByPostId(Long postId);
    void deleteByPostId(Long postId);
}