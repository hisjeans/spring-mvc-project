package com.codeit.mvc.service;

import com.codeit.mvc.domain.Comment;
import com.codeit.mvc.dto.response.CommentResponse;
import com.codeit.mvc.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j // 로그 찍기 위해
@RequiredArgsConstructor
public class CommentService {
    // 서비스가 데이터베이스에 직접 넣지 않기 때문(repository 담당)
    private final CommentRepository commentRepository;

    public CommentResponse createComment(Long postId, String author, String content){ // 3 데이터가 controller에서 넘어올 것
        // 검증 필요 - 공백만 넘어올 수도 있고, 비어 있을 수도 있기 때문
        // try-catch 대신 예외를 생성해 발생시키는 이유:
        // 작성자가 아무것도 작성하지 않고 보낸 것을 대체 코드로 작성해줄 수 없음 - 예외를 발생시켜 막아야 함
        // 다음 코드인 commentRepository.save가 실행되면 안 되기 때문에 에러 처리가 필요한 것
        // return 으로 작성하면 되지 않나? - 리턴 타입이 void가 아니라면 무엇을 리턴해야 하는가
        // 발생한 예외는 예외 처리 필요
        // 메서드를 호출한 쪽으로 예외를 던지고 컨트롤러에서 try-catch 일일이 들어가게 됨 - 나중에 해볼 것
        if (author == null || author.isBlank()){
            throw  new IllegalArgumentException("작성자는 비워둘 수 없습니다.");
        }
        if (content == null || content.isBlank()){
            throw new IllegalArgumentException("댓글 내용은 비워둘 수 없습니다.");
        }
        Comment comment = Comment.builder()
                .postId(postId)
                .author(author)
                .content(content)
                .build();

        Comment saved = commentRepository.save(comment);
        return CommentResponse.from(saved);

    }

    public List<CommentResponse> getCommentsByPostId(Long id) {
        List<Comment> list = commentRepository.findByPostId(id);
        // entity를 바로 리턴하는 것은 옳지 않기 때문에 comment->dto 변환이 옳은데 일일이 변환할 필요 없이 stream으로 작성하자❕
        return list.stream()
                .map(comment -> CommentResponse.from(comment))
                .collect(Collectors.toList()); // 객체 형태를 바꾸기 때문에 -> map

// 기본 방식을 쓰고 stream으로 고쳐쓰는 방식으로 연습하자
// ArrayList<Comment> dtoList = new ArrayList<>();
//        for (Comment comment : dtoList) {
//            CommentResponse dto=CommentResponse.from(comment);
//            dtoList.add(dto);
//        }
//        return dtoList;
    }
}
