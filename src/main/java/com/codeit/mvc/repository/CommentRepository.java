package com.codeit.mvc.repository;

import com.codeit.mvc.domain.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {

    // 단일 조회
    Optional<Comment> findById(Long id);

    // 댓글 저장
    Comment save(Comment comment);

    // 댓글 삭제
    void deleteById(Long id);

    // 게시물 번호로 댓글 조회
    List<Comment> findByPostId(Long postId);
}
