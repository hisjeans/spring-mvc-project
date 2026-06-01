package com.codeit.mvc.service;


import com.codeit.mvc.domain.Category;
import com.codeit.mvc.domain.Post;
import com.codeit.mvc.dto.request.PostRequest;
import com.codeit.mvc.dto.response.PostResponse;
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

    public List<PostResponse> searchPost(String keyword, Category category, String sort) {

        // 지금은 DB가 없기 때문에 Java 문법으로 조건부 탐색과 정렬 기준을 직접 작성하고 있지만
        // 추후에는 DB에게 SQL로 명령을 내릴 것
        List<Post> posts;

        if (category!=null){ // 카테고리가 있는가
            posts=postRepository.findByCategory(category);
        } else if (keyword!=null&&!keyword.trim().isEmpty()) { // 키워드가 있는가
            posts=postRepository.findByTitleOrContentContaining(keyword);
        } else { // 아무것도 없는가(예: 사용자가 아무것도 입력하지 않고 검색하는 경우)
            posts=postRepository.findAll();
        }

        // 정렬
        // 최신순 기준
        if ("viewCount".equals(sort)) {
            // viewCount는 null이 들어갈 수 없기 때문에 특정 변수 문자값과 문자상수를 비교할 때는 문자상수를 앞에 두는 것을 선호

            posts=posts.stream()
                    .sorted(Comparator.comparing(Post::getViewCount).reversed())
                    .collect(Collectors.toList());
        } else {
            posts=posts.stream()
                    .sorted(Comparator.comparing(Post::getCreatedAt).reversed())
                    .collect(Collectors.toList());
        }
        return posts.stream()
                .map(post -> PostResponse.from(post))
                .collect(Collectors.toList());
        // 갯수는 그대로 유지하고 객체의 형태만 post->dto - Map 이용하자
    }

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

    public PostResponse getPostById(Long id){
        Post post = postRepository.findById(id) // 객체 제대로 있으면 post에 담고
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다.")); // 없다면 예외 발생
        // if문 사용하지 않고 optional 통해 간단히 표현할 수 있는 것
        post.setViewCount();
        return PostResponse.from(post);
    }


}
