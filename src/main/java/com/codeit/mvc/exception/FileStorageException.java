package com.codeit.mvc.exception;
// 파일 저장, 삭제, 조회 등에서 발생하는 런타임 예외
public class FileStorageException extends RuntimeException {
    public FileStorageException(String message) {
        super(message);
    } // 단순 메시지만 남기면 원인 파악, 연쇄적인 예외 확인하기 어려움
    // 예외 발생 시 메시지만 받아 노출시키고 끝

    public FileStorageException(String message, Throwable cause) {
        super(message, cause);
    } // 예외 발생 시 메시지와 예외 객체 자체를 전달받아서 상위 계층에 전파
    // Throwable > exception 상위, throwable 사용하는 것이 관례
    // 이렇게 해야 예외의 원인을 역추적했을 때 진짜 원인 등을 콘솔에서 확인 가능
}
