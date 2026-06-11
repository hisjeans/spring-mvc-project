package com.codeit.mvc.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/*
전역 예외 처리 - @ControllerAdvice

모든 컨트롤러에서 던져진 예외를 한 곳에서 가로채 상황에 맞는 에러 페이지로 응답한다
 */
@ControllerAdvice // controller 담고 있음, 빈 등록
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(PostNotFoundException.class) // 예외를 다룰 수 있는 메서드, PostNotFoundException 클래스 다룰 것
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFound(PostNotFoundException e, Model model){ // 모델(화면단 전달 바구니) 달라고 할 수 있음
        // 자동으로 호출될 것
        log.warn("자원 없음: {}", e.getMessage());
        model.addAttribute("message", e.getMessage());
        // 사용자에게도 알려줘야 한다
        return "error/404";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleBadRequest(IllegalArgumentException e, Model model){
        log.warn("잘못된 요청: {}", e.getMessage());
        model.addAttribute("message", e.getMessage());
        return "error/400";
    }

    @ExceptionHandler(FileStorageException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleFileStorage(FileStorageException e, Model model){
        log.warn("서버 오류(파일 입출력): {}", e.getMessage());
        model.addAttribute("message", e.getMessage());
        return "error/500";
    }

    // 그 밖에 예상 못한 모든 예외
    @ExceptionHandler(Exception.class) // 모든 예외의 부모로 선언
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleException(Exception e, Model model){
        log.warn("서버 오류", e);
        model.addAttribute("message", "서버에서 문제가 발생했습니다. 잠시후 다시 시도해주세요.");
        return "error/500";
    }

}
// Controller에서 발생한 예외는 Controller Advice가 잡는다
// RestController에서 발생한 예외는 RestController Adivice가 잡는다
