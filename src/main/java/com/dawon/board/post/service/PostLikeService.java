package com.dawon.board.post.service;

import com.dawon.board.common.NotFoundException;
import com.dawon.board.post.dto.LikeResponse;
import com.dawon.board.post.mapper.PostLikeMapper;
import com.dawon.board.post.mapper.PostMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostLikeService {

    private final PostMapper postMapper;
    private final PostLikeMapper postLikeMapper;

    @Transactional
    public LikeResponse toggle(Long postId, Long memberId) {
        if (postMapper.findById(postId) == null) {
            throw new NotFoundException("게시글이 존재하지 않습니다.");
        }

        boolean liked;
        if (postLikeMapper.exists(postId, memberId)) {
            postLikeMapper.delete(postId, memberId);
            liked = false;
        } else {
            try {
                postLikeMapper.insert(postId, memberId);
            } catch (DuplicateKeyException e) {
            }
            liked = true;
        }
        return new LikeResponse(liked, postLikeMapper.countByPostId(postId));
    }

    @Transactional(readOnly = true)
    public boolean isLiked(Long postId, Long memberId) {
        return postLikeMapper.exists(postId, memberId);
    }
}