package com.codeit.mvc.exception;

// 요청한 id의 게시물이 없을 때 던지는 예외
public class PostNotFoundException extends RuntimeException {
    public PostNotFoundException(Long id) {
        super("게시글을 찾을 수 없습니다. id="+id); // 메시지 전달
    }
}
