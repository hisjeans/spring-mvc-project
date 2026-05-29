package com.codeit.mvc.domain;

public enum Category {

    TECH("기술"),
    LIFE("일상"),
    TRABLE("여행"),
    FOOD("음식"),
    HOBBY("취미");

    // 필드 선언도 가능
    // final 필드 setter로 채울 수 없음 - final 불변이기 때문에 setter 존재할 수 없음, 만일 존재한다 하더라도 final 필드 초기화 - 객체 생성될 때 값이 결정
    private final String displayName;

    Category(String displayName){
        this.displayName=displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
