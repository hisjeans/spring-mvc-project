package com.codeit.mvc.config;

import com.codeit.mvc.domain.Category;
import com.codeit.mvc.domain.Post;
import com.codeit.mvc.repository.PostRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(PostRepository postRepository) {
        // CommandLineRunner 타입 객체는 Spring 어플리케이션 초기화가 모두 완료되고, 빈 등록까지 전부 완료되었을 때
        // 마지막으로 실행되는 객체입니다.

        return args -> {
            // CommandLineRunner 인터페이스 구현체를 람다식으로 즉석에서 선언.
            // 샘플 게시글 생성
            Post post1 = new Post(
                    "Spring MVC 시작하기",
                    "Spring MVC는 웹 애플리케이션을 개발하기 위한 강력한 프레임워크입니다. " +
                            "Model-View-Controller 패턴을 기반으로 하여 관심사의 분리를 통해 유지보수가 쉬운 코드를 작성할 수 있습니다.",
                    "김개발",
                    Category.TECH
            );
            postRepository.save(post1);

            Post post2 = new Post(
                    "RESTful API 설계 원칙",
                    "REST는 Representational State Transfer의 약자로, 웹의 장점을 최대한 활용할 수 있는 아키텍처 스타일입니다. " +
                            "HTTP 메서드를 적절히 사용하고, 리소스 중심의 URL 설계가 핵심입니다.",
                    "박코딩",
                    Category.TECH
            );
            postRepository.save(post2);

            Post post3 = new Post(
                    "제주도 여행 후기",
                    "제주도의 아름다운 자연을 만끽하고 왔습니다. 한라산 등반과 바다 드라이브가 특히 인상 깊었습니다. " +
                            "맛있는 현지 음식들도 많이 먹어보았는데, 흑돼지 고기와 해산물이 일품이었습니다.",
                    "이여행",
                    Category.TRABLE
            );
            postRepository.save(post3);

            Post post4 = new Post(
                    "집에서 만드는 파스타",
                    "집에서도 레스토랑 못지않은 맛있는 파스타를 만들 수 있습니다. " +
                            "신선한 재료와 올바른 조리법만 알면 누구나 쉽게 만들 수 있어요. " +
                            "오늘은 크림 파스타 레시피를 공유합니다.",
                    "최요리",
                    Category.FOOD
            );
            postRepository.save(post4);

            Post post5 = new Post(
                    "나의 일상 이야기",
                    "요즘 새로운 취미를 시작했습니다. 바로 독서인데요, " +
                            "하루에 30분씩이라도 책을 읽으니 생활에 활력이 생기는 것 같아요. " +
                            "여러분도 새로운 취미를 시작해보시는 건 어떨까요?",
                    "정일상",
                    Category.LIFE
            );
            postRepository.save(post5);

            Post post6 = new Post(
                    "사진 촬영 초보 가이드",
                    "DSLR 카메라를 처음 구매했다면 이것부터 알아야 합니다. " +
                            "조리개, 셔터 스피드, ISO 등 기본적인 개념들을 이해하면 " +
                            "훨씬 더 좋은 사진을 찍을 수 있습니다.",
                    "강사진",
                    Category.HOBBY
            );
            postRepository.save(post6);

            Post post7 = new Post(
                    "Spring Boot 자동 설정의 마법",
                    "Spring Boot의 가장 큰 장점 중 하나는 자동 설정입니다. " +
                            "@SpringBootApplication 어노테이션 하나로 수많은 설정이 자동으로 이루어집니다. " +
                            "오늘은 이 자동 설정의 원리에 대해 알아보겠습니다.",
                    "김개발",
                    Category.TECH
            );
            postRepository.save(post7);

            Post post8 = new Post(
                    "건강한 아침 식사 루틴",
                    "아침을 거르지 않고 건강하게 먹는 것이 하루를 시작하는 가장 좋은 방법입니다. " +
                            "간단하지만 영양가 있는 아침 메뉴들을 소개합니다.",
                    "최요리",
                    Category.FOOD
            );
            postRepository.save(post8);

            System.out.println("✅ 초기 데이터 생성 완료: " + postRepository.findAll().size() + "개의 게시글");
        };
    }

}
