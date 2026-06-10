package com.codeit.mvc.dto.response;

import com.codeit.mvc.domain.Comment;

import java.time.LocalDateTime;

public record CommentResponse(
        Long id,
        String content,
        String author,
        LocalDateTime createdAt
) {
    // entity와 달리 postid는 담지 않음 - 프론트 단에서 필요하지 않기 때문에 굳이 담을 필요 없는 것
    // 요청과 함께 넘어오는 데이터가 entity와 항상 같지 않고, 화면단과 함께 넘어오는 데이터가 항상 entity와 같지 않기 때문에 항상 dto 각각 생성 필요한 것
    public static CommentResponse from(Comment comment){ // comment로 부터 만든다는 의미에서 'from' 으로 이름 지음
        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getAuthor(),
                comment.getCreatedAt()
        ); // 서비스에서 from이란 객체를 통해 일일이 작성하지 않고 값 전달받도록 함
    }
}
