package com.codeit.mvc.service;


import com.codeit.mvc.domain.Post;
import com.codeit.mvc.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;

    public List<Post> getAllPosts() {
        // 리턴 타입 list<post> 타입으로도 가능하지만 생성일자 내림차순 정렬하고자 함 postRespository.findAll();
        return postRepository.findAll().stream()
                .sorted(Comparator.comparing(Post::getCreatedAt).reversed()) // 생성일자 내림차순
                .collect(Collectors.toList());
    }

}
