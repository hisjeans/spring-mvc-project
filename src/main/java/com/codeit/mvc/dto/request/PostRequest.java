package com.codeit.mvc.dto.request;

import com.codeit.mvc.domain.Category;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// DTO: Data Transfer Object
// 데이터 전송 및 API 스펙 맞춤용 객체, 서비스에게 데이터 운반 용도
// Entity는 데이터베이스와 굉장히 밀접한 연관을 가지고 있기 때문에
// 문제1. DB 스펙이 곧 입력값 스펙이 되는 건 좋지 않음
// 문제2. 보안적인 문제 발생 가능 - 응답용 DTO 만들 때 사용 ( 응답을 줄 때도 DTO 사용해 꼭 필요한 정보만 화면단에 내려야 함)
// 예: User->Id, pw 화면단에 내려주는 건 위험, 지양해야 함, 또한 화면단에서 굳이 필요없는 정보일 수 있음
// 반대로 entity 보다 더 많은 정보 요청될 수 있음
// 문제 3. 화면단에서 요구하는 데이터가 Entity보다 더 적을 수도 있고, 더 많을 수도 있다
// DTO가 나중에 API 스펙이 될 수 있음

public record PostRequest (

    String title,
    String content,
    String author,
    Category category,
    String thumbnailPath

) {
    // body는 없으면 적지 않아도 됨
    // 문제: setter가 없어지지 않나? - record는 불변 객체, setter 지원하지 않음
    public PostRequest withThumbnailPath(String fileName){
        return  new PostRequest(title, content, author, category, thumbnailPath);
        // 레코드는 특정 필드만 변경하는 setter는 제공되지 않음 - private final
        // 아예 새로운 객체로
    }
}
// 새로운 클래스를 제작하는데 DTO를 제작하고 싶다면 -> record로 작성핮