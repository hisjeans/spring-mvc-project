package com.codeit.mvc.service;


import com.codeit.mvc.domain.Post;
import com.codeit.mvc.dto.request.PostRequest;
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

    public Post createPost(PostRequest postRequest) {

        Post post = new Post(
                postRequest.getTitle(),
                postRequest.getContent(),
                postRequest.getAuthor(),
                postRequest.getCategory()
        );
        return postRepository.save(post);
    }

    public void getPostById(Long id){
        Post post = postRepository.findById(id) // 객체 제대로 있으면 post에 담고
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다.")); // 없다면 예외 발생
        // if문 사용하지 않고 optional 통해 간단히 표현할 수 있는 것
        post.setViewCount();
    }


}
