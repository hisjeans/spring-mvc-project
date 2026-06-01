package com.codeit.mvc.repository;

import com.codeit.mvc.domain.Category;
import com.codeit.mvc.domain.Post;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository // @Component 가능하나 repository 기능 의미 부여 목적으로 @Repository
public class MemoryPostRepository implements PostRepository{

    // 객체 바꿀 수 없도록 private final
    // 맵의 키만으로 포스트번호 조회 가능하도록
    // ConcurrentHashMap 멀티 스레드에서 안전한 hashmap, 여러 스레드에서 안전한 hash map
    // 기존에 사용하던 hash map은 싱글스레드 hash map
    // 예: map(1번 게시물) map(2번 게시물)... 첫번째 맵을 조회할 때 2번 게시물 조회되지 않음, 스레드 간 공유되지 않는 문제
    // => ConcurrentHashMap 스레드 간 공유 가능하도록
    private final Map<Long, Post> store=new ConcurrentHashMap<>();
    private final AtomicLong sequence=new AtomicLong(1L);
    // 게시물 작성할 때 사용자는 제목, 작성자(로그인 성공
    // 아이디는 UUID 통해 랜덤한 문자열과 숫자 섞인 값
    // 게시글 등록될 때마다 순서대로 올려주는 것이 가장 쉬울 것 <- 사용자는 모름, 해당값을 올려주자
    // 멀티 스레드 환경에서 동기화 없이도 안전하게 변경할 수 있는 long 타입의 변수 AtomicLong
    // 예: 글 번호 5번까지 올라갔다 가, A,B insert 동시 요청 - A,B 스레드 모두 count++
    // - 6(A count++)을 건너뛰고 7(B count++)이 저장될 수 있음, 특정 스레드 누락되는 현상 발생 가능
    // AtomicLog A count++ 동안 다른 스레드 block A 스레드가 올리고 저장할 때까지 기다리도록 해 안전성 보장
    // 동시성 이슈 발생 상황에서 안전하게 count 늘릴 수 있도록 함
    // 글 등록할 때마다 sequence 증가시켜 아이디 구분할 수 있도록 함
    // 초기값 1 설정, 1번 부터 시작
    // (참고) 데이터베이스에서는 자동으로 올려주는 기능 존재


    @Override
    public List<Post> findAll() {
        return new ArrayList<>(store.values()); // 맵에서 value들만 꺼내 넣겠다
    }

    @Override
    public Optional<Post> findById(Long id) {
        // id에 해당하는 post 꺼낼 수 있음
        return Optional.ofNullable(store.get(id));
        // 리스트 get <- index, map <- key
        // 맵은 키에 해당하는 값이 없으면 null 줌 - 데이터베이스도 마찬가지
        // optional로 감싸자
        // Optional.ofNullable null 들어갈 수 있는 optional
        // Optional.of <- null 들어갈 수 없음, null이 들어가는 것을 거부하기 때문에 예외 발생
        // Null 허용하는 Optional 객체 생성해서 리턴, of()는 null은 허용하지 않음
    }

    @Override
    public Post save(Post post) {
        // controller->service->repository
        // service에서 유효성이 검증된 post 객체가 들어오는 것
        // post는 현재 id가 없음 - id는 사용자가 매기는 값이 아님, id는 실제 저장소 save되기 전에 올리는 것이 가장 안전한 방법
        // 레파지토리가 채워줘야 함(바로 put하면 안 됨)
        if (post.getId()==null){ // 당연히 null일 것
            post.setId(sequence.getAndIncrement());
            // 자동으로 숫자 올려 가지고 올 것
            // 다음으로 등록할 게시물 위해 숫자를 올려야 함
        }
        store.put(post.getId(),post);
        return post;
    }

    @Override
    public void deleteById(Long id) {
        store.remove(id);

    }

    @Override
    public List<Post> findByCategory(Category category) {
        return store.values().stream()
                .filter(post -> post.getCategory()==category)
                .collect(Collectors.toList());
    }

    @Override
    public List<Post> findByTitleContaining(String title) {
        return store.values().stream()
                .filter(post -> post.getTitle().toLowerCase().contains(title.toLowerCase()))
                // post -> post.getTitle().equals(title) title이 완벽히 똑같아야만 가져올 수 있는 것, 본래 의도 아님
                // 포함하고 있다면, 영문으로 되어 있다면 대소문자 구분 없이 전부 포함시키자
                .collect(Collectors.toList());
    }

    @Override
    public List<Post> findByTitleOrContentContaining(String keyword) {
        return store.values().stream()
                .filter(post -> post.getTitle().contains(keyword)
                        ||post.getContent().contains(keyword))
                .collect(Collectors.toList());
    }

    @Override
    public void updateViewCount(Long id) {

        store.get(id).setViewCount();
    }
}
