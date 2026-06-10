package com.codeit.mvc.domain;

import lombok.*;

import java.time.LocalDateTime;

@Getter
//@NoArgsConstructor // 아무것도 받지 않는 생성자
@AllArgsConstructor // 5개 필드 모두 받아 초기화해주는 생성자
public class Comment {

    @Setter
    private Long id; // repository 에서 채워질 것
    // 댓글은 반드시 특정 게시물에 속할 것
    private Long postId;
    private String content;
    private String author;
    private LocalDateTime createdAt;

    // id, createdAt은 우리가 입력하는 값이 아니기
    // 물론 @Builder 달아도 되지만 이렇게 할 경우 id도 채워넣을 수 있게 되기 때문에 생성자로

    public Comment() { // 기본 생성자로 createdAt 추가되도록
        this.createdAt=LocalDateTime.now();
    }

    @Builder
    public Comment(Long postId, String content, String author) {
        this();
        this.postId = postId;
        this.content = content;
        this.author = author;
    }
}
