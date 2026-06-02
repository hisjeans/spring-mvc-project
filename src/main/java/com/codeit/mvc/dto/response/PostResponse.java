package com.codeit.mvc.dto.response;


import com.codeit.mvc.domain.Category;
import com.codeit.mvc.domain.Post;

import java.time.LocalDateTime;

// record: java 16부터 정식 도입된 클래스 문법
// 1. 모든 필드를 private final로 만들어 줌 - record 목적: 불변 데이터 객체를 위해 특화시켜 만든 새로운 클래스
// 2. getter 메서드가 자동 생성 - record가 제공하는 getter는 필드명과 메서드명이 같음 (get이 붙지 않음)
// 3. 생성자, equals(), hashCode(), toString()이 자동 구현, Lombok 사용할 필요가 없음
// 4. Builder 사용은 지양하는 편 (생성자가 기본으로 제공되므로 생성자를 쓰는 것을 선호)
public record PostResponse (

    Long id,
    // id는 int로 선언해도 되나 더 넓은 범위까지 허용하기 위해 Long 선언 일반적
    // 기본 타입 long으로도 선언 가능하나 객체 타입 Long으로 선언하면 null이 들어갈 수 있음
    // Long 제공하는 여러 메서드 사용 가능
    String title,
    String content,
    String author,
    Category category,
    int viewCount,
    // 필요한 경우 특정 필드에 @Setter 선택적으로 쓸 수 있음
    String thumbnailPath,
    LocalDateTime createdAt,
    LocalDateTime updatedAt

) { // 중괄호 안 메서드 선언 가능

    public static PostResponse from(Post post) {
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getAuthor(),
                post.getCategory(),
                post.getViewCount(),
                post.getThumbnailPath(),
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }
}


// 레코드 선언