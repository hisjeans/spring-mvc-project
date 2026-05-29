package com.codeit.mvc.repository;

import com.codeit.mvc.domain.Category;
import com.codeit.mvc.domain.Post;

import java.util.List;
import java.util.Optional;

public interface PostRespository {

    // 전체 조회
    List<Post> findAll();

    // id 제외한 모든 필드 중복 가능
    // id는 항상 고유한 값이어야 함(중복 불가능)
    // id로 게시글 조회
    // id가 잘못된 값으로 들어왔을 때 처리하기 위해 Optional로 포장해 사용
    Optional<Post> findById(Long id);

    // 게시물 등록
    // 데이터 전부 일일이 받지 말고 'Post post'
    // void로 선언 가능, 돌려줄 것이 없다는 의미에서 <<사용비중<< Post는 그대로 저장된 객체 돌려준다는 의미
    // null 갈 일 없기 때문(애초에 객체가 비어 있다면 안 되기 때문에) Optional 필요 없음
    Post save(Post post);

    // 삭제
    // 지우고 리턴할 값 없음 void
    void deleteById(Long id);

    // 카테고리 조회
    // 카테고리 중복 가능, 여러 값 받기 위해 list
    List<Post> findByCategory(Category category);

    // 제목으로 조회
    // 사용자가 완벽히 일치하는 제목으로 조회하는 경우 거의 없음
    // 타이틀을 포함하는 게시물 모두 조회하는 방향으로 List(타이틀 포함하는 게시물 여러개일 수 있기 때문)
    List<Post> findByTitleContaining(String title);

    // 제목 또는 내용으로 조회
    // 특정 키워드가 제목 또는 내용에 포함되어 있다면
    // 포스트 여러개 리턴될 수 있기 때문에 List
    List<Post> findByTitleOrContentContaining(String keyword);

    // 조회수 올림
    // 고유하게 게시물 식별할 수 있는 값 id 필요
    // 특별히 리턴할 값이 없다면 void
    void updateViewCount(Long id);



}
