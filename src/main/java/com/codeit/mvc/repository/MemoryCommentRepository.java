package com.codeit.mvc.repository;

import com.codeit.mvc.domain.Comment;
import com.codeit.mvc.domain.Post;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class MemoryCommentRepository implements CommentRepository {

    private final Map<Long, Comment> store=new ConcurrentHashMap<>();
    private final AtomicLong sequence=new AtomicLong(1L);

    @Override
    public Optional<Comment> findById(Long id) {
        return Optional.ofNullable(store.get(id)); // 특정 id 찾았을 때 없을 수 있기 때문 Nullable
    }

    @Override
    public Comment save(Comment comment) {
        if (comment.getId()==null){
            comment.setId(sequence.getAndIncrement()); // incrementAndGet 하면 안 됨‼️
        }
        store.put(comment.getId(), comment);
        return comment;
    }

    @Override
    public void deleteById(Long id) {
        store.remove(id);
    }

    @Override
    public List<Comment> findByPostId(Long postId) {
        return store.values().stream()
                .filter(comment -> comment.getPostId()==postId) // Long 타입 auto boxing, auto unboxing 제공 객체->기본 타입 리턴 자유로움
                .collect(Collectors.toList());
    }
}
