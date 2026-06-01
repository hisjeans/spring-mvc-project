package com.codeit.mvc.dto.response;

import com.codeit.mvc.domain.Category;
import com.codeit.mvc.domain.Post;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

// 응답용 DTO: 응답 과정에서도 Entity를 직접 리턴하는 것은 화면단에서 요구되는 데이터와 일치하지 않을 수 있음
// (너무 과하거나 부족한 경우도 생길 수 있다)
// 각 응답에 맞는 DTO를 제작해서 응답을 주자
// @Setter 추가? - getPostById에서 postResponse 객체 생성해 일일이 추가할 수 있지만 불편
// @AllArgsConstructor response 객체 생성해 post에서 getId()... 순차적으로 나열할 수 있음
// getPostById뿐만 아니라 앞으로 다른 데에서도 추가해야 하는 경우가 많이 생길 것 같다
// PostResponse에서 메서드 생성해 getPostById에서 PostResponse resDto=PostResponse.from(Post post)
// : 정적 팩토리 메서드
// 생성자의 매개값으로 값 전달하는 것도 괜찮지만 생성자(객체 생성, 초기화..)도 메서드의 일종 - 전달 순서가 굉장히 중요
// 전달 순서가 틀렸을 때 이를 문법적으로 막을 수 있는 기능이 없음
// setter < 생성자를 부르는 편이 좋지만 생성자는 값 전달 순서가 중요
//
@Getter
@Builder
public class PostResponse {

    // DTO의 필드가 Entity와 동일하기는 하지만, 그래도 DTO로 변환해 내리는 것을 추천
    // 예컨대 날짜를 가공해 응답하거나, 이미지 경로 등을 가공해 내려야 할 필용성도 있음
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

    public static PostResponse from(Post post) {
        return PostResponse.builder() // static 메서드
                .id(post.getId())
                .title(post.getTitle()) // 전달받은 title을 builder 내부에 setting
                .thumbnailPath(post.getThumbnailPath())
                .author(post.getAuthor())
                .content(post.getContent()) // 전달받은 content를 builder 내부에 setting하고 리턴
                .viewCount(post.getViewCount())
                .category(post.getCategory())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt()) // 값이 전부 채워진 상태
                .build(); // 새로운 PostResponse 객체를 생성하면서 this, builder를 보냄
        // builder에서 id 꺼내 채우고.. 전부 채워진 PostResponse 리턴되는 것, 원본 객체 받을 수 있음
        // builder 패턴 많이 사용
    }
}
