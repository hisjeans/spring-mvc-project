package com.codeit.mvc.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

// Entity: DB와 Java 객체를 연결해주는 가장 핵심적 개념
// DB 테이블과 1:1로 매칭되는 필드를 선언
// 데이터베이스와 가장 닮아있는 순수한 객체로 이해할 수 있음
@Getter
// @Setter <- Entity에서 Setter 사용은 권장하지 않음‼️
// Entity는 데이터베이스와 가장 닮아 있는 객체이고, DB 조회한 결과를 Post 클래스 사용할 것
// Setter 사용하면 데이터베이스 내용 임의로 수정할 가능성 존재, 데이터베이스의 내용을 Post 안에서 수정하지 말 것‼️
// 예: 레파지토리가 마음대로 author를 바꾸면 서비스나 컨트롤러(다른 계층)는 이 사실을 알 수 없음 - 무결성, 결점이 없어야 한다는 사실 깨짐
// 필요한 필드에만 선택적으로 생성함 (예: 썸네일경로 DB에서 조금 더 추가되어야 한다, 가공되어야 한다)
// 모든 필드에 사용하는 것은 지양하자
// ⚠️ 특히 id는 주의 필요️
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    @Setter
    private Long id;
    // id는 int로 선언해도 되나 더 넓은 범위까지 허용하기 위해 Long 선언 일반적
    // 기본 타입 long으로도 선언 가능하나 객체 타입 Long으로 선언하면 null이 들어갈 수 있음
    // Long 제공하는 여러 메서드 사용 가능
    private String title;
    private String content;
    private String author;
    private Category category;
    private int viewCount;
    // 필요한 경우 특정 필드에 @Setter 선택적으로 쓸 수 있음
    private String thumbnailPath;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Lombok의 setter는 완전 기본형, 커스텀을 원한다면 직접 setter 구축하자
    public void setViewCount(){
        this.viewCount++; // 매개값 받지 않고 viewcount 올리겠다
    }
}
