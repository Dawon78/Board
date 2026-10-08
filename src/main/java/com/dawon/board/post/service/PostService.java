package com.dawon.board.post.service;

import com.dawon.board.common.NotFoundException;
import com.dawon.board.post.domain.Post;
import com.dawon.board.post.dto.PostForm;
import com.dawon.board.post.dto.PostView;
import com.dawon.board.post.mapper.PostLikeMapper;
import com.dawon.board.post.mapper.PostMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private static final int PAGE_SIZE = 10;
    private final PostLikeMapper postLikeMapper;
    private final PostMapper postMapper;

    @Transactional(readOnly = true)
    public List<PostView> findPage(int page) {
        int offset = (page - 1) * PAGE_SIZE;
        return postMapper.findPage(offset, PAGE_SIZE);
    }

    @Transactional(readOnly = true)
    public int totalPages() {
        int total = postMapper.countAll();
        return Math.max(1, (int) Math.ceil((double) total / PAGE_SIZE));
    }

    @Transactional(readOnly = true)
    public PostView findDetail(Long id) {
        PostView post = postMapper.findDetail(id);
        if (post == null) {
            throw new NotFoundException("게시글이 존재하지 않습니다.");
        }
        return post;
    }

    @Transactional
    public Long create(PostForm form, Long memberId) {
        Post post = new Post();
        post.setMemberId(memberId);
        post.setTitle(form.getTitle());
        post.setContent(form.getContent());
        postMapper.insert(post);
        return post.getId();
    }

    @Transactional(readOnly = true)
    public PostForm getEditForm(Long postId, Long loginMemberId) {
        Post post = getOwnedPost(postId, loginMemberId);
        PostForm form = new PostForm();
        form.setTitle(post.getTitle());
        form.setContent(post.getContent());
        return form;
    }

    @Transactional
    public void update(Long postId, PostForm form, Long loginMemberId) {
        Post post = getOwnedPost(postId, loginMemberId);
        post.setTitle(form.getTitle());
        post.setContent(form.getContent());
        postMapper.update(post);
    }

    @Transactional
    public void delete(Long postId, Long loginMemberId) {
        getOwnedPost(postId, loginMemberId);
        postLikeMapper.deleteByPostId(postId);
        postMapper.delete(postId);
    }

    private Post getOwnedPost(Long postId, Long loginMemberId) {
        Post post = postMapper.findById(postId);
        if (post == null) {
            throw new NotFoundException("게시글이 존재하지 않습니다.");
        }
        if (!post.getMemberId().equals(loginMemberId)) {
            throw new AccessDeniedException("작성자만 수정·삭제할 수 있습니다.");
        }
        return post;
    }
}