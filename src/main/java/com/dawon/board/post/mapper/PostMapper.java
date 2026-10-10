package com.dawon.board.post.mapper;

import com.dawon.board.post.domain.Post;
import com.dawon.board.post.dto.PostView;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PostMapper {
    void insert(Post post);
    Post findById(Long id);                                   // 수정·삭제 전 "작성자 확인"용
    PostView findDetail(Long id);                             // 상세 화면용
    List<PostView> findPage(@Param("offset") int offset,
                            @Param("size") int size);         // 목록 화면용 (페이징)
    List<PostView> findPage(@Param("offset") int offset, @Param("size") int size,
                            @Param("type") String type, @Param("keyword") String keyword);
    int count(@Param("type") String type, @Param("keyword") String keyword);
    void update(Post post);
    void delete(Long id);
}