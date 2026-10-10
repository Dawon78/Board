package com.dawon.board.comment.service;

import com.dawon.board.comment.domain.Comment;
import com.dawon.board.comment.mapper.CommentLikeMapper;
import com.dawon.board.comment.mapper.CommentMapper;
import com.dawon.board.common.NotFoundException;
import com.dawon.board.post.dto.LikeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentLikeService {

    private final CommentMapper commentMapper;
    private final CommentLikeMapper commentLikeMapper;

    @Transactional
    public LikeResponse toggle(Long commentId, Long memberId) {
        Comment comment = commentMapper.findById(commentId);
        if (comment == null || comment.isDeleted()) {
            throw new NotFoundException("댓글이 존재하지 않습니다.");
        }

        boolean liked;
        if (commentLikeMapper.exists(commentId, memberId)) {
            commentLikeMapper.delete(commentId, memberId);
            liked = false;
        } else {
            try {
                commentLikeMapper.insert(commentId, memberId);
            } catch (DuplicateKeyException e) {
            }
            liked = true;
        }
        return new LikeResponse(liked, commentLikeMapper.countByCommentId(commentId));
    }
}