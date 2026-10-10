package com.dawon.board.comment.service;

import com.dawon.board.comment.domain.Comment;
import com.dawon.board.comment.dto.CommentForm;
import com.dawon.board.comment.dto.CommentView;
import com.dawon.board.comment.mapper.CommentLikeMapper;
import com.dawon.board.comment.mapper.CommentMapper;
import com.dawon.board.common.NotFoundException;
import com.dawon.board.post.mapper.PostMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentMapper commentMapper;
    private final PostMapper postMapper;
    private final CommentLikeMapper commentLikeMapper;


    @Transactional(readOnly = true)
    public List<CommentView> findByPostId(Long postId, Long memberId) {
        return commentMapper.findByPostId(postId, memberId);
    }

    @Transactional
    public Long create(Long postId, CommentForm form, Long memberId) {
        if (postMapper.findById(postId) == null) {
            throw new NotFoundException("게시글이 존재하지 않습니다.");
        }
        Comment comment = new Comment();
        comment.setPostId(postId);
        comment.setMemberId(memberId);
        comment.setContent(form.getContent());
        comment.setParentId(resolveParentId(postId, form.getParentId()));
        commentMapper.insert(comment);
        return comment.getId();
    }

    @Transactional
    public Long update(Long commentId, CommentForm form, Long memberId) {
        Comment comment = getOwnedComment(commentId, memberId);
        comment.setContent(form.getContent());
        commentMapper.update(comment);
        return comment.getPostId();
    }

    private Long resolveParentId(Long postId, Long parentId) {
        if (parentId == null) {
            return null;
        }
        Comment parent = commentMapper.findById(parentId);
        if (parent == null || !parent.getPostId().equals(postId) || parent.isDeleted()) {
            throw new NotFoundException("답글을 달 댓글이 존재하지 않습니다.");
        }
        return parent.getParentId() != null ? parent.getParentId() : parent.getId();
    }

    private Comment getOwnedComment(Long commentId, Long memberId) {
        Comment comment = commentMapper.findById(commentId);
        if (comment == null || comment.isDeleted()) {
            throw new NotFoundException("댓글이 존재하지 않습니다.");
        }
        if (!comment.getMemberId().equals(memberId)) {
            throw new AccessDeniedException("작성자만 수정·삭제할 수 있습니다.");
        }
        return comment;
    }

    @Transactional
    public Long delete(Long commentId, Long memberId) {
        Comment comment = getOwnedComment(commentId, memberId);

        if (comment.getParentId() == null) {
            if (commentMapper.countReplies(comment.getId()) > 0) {
                commentMapper.softDelete(comment.getId());
            } else {
                commentLikeMapper.deleteByCommentId(comment.getId());
                commentMapper.delete(comment.getId());
            }
        } else {
            commentLikeMapper.deleteByCommentId(comment.getId());
            commentMapper.delete(comment.getId());
            Comment parent = commentMapper.findById(comment.getParentId());
            if (parent != null && parent.isDeleted() && commentMapper.countReplies(parent.getId()) == 0) {
                commentLikeMapper.deleteByCommentId(parent.getId());
                commentMapper.delete(parent.getId());
            }
        }
        return comment.getPostId();
    }
}