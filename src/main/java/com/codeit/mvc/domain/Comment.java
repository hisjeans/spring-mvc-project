package com.codeit.mvc.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @Setter
    private Long id;

    // 댓글은 반드시 특정 게시물에 속할 것
    private Long postId;

    private String content;
    private String author;
    private LocalDateTime createdAt;
}
