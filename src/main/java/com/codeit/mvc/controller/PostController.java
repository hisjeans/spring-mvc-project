package com.codeit.mvc.controller;

import com.codeit.mvc.domain.Category;
import com.codeit.mvc.domain.Post;
import com.codeit.mvc.dto.request.PostRequest;
import com.codeit.mvc.dto.response.CommentResponse;
import com.codeit.mvc.dto.response.PostResponse;
import com.codeit.mvc.service.CommentService;
import com.codeit.mvc.service.FileService;
import com.codeit.mvc.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller // 컨트롤러가 요청에 맞는 뷰 페이지를 결정 (Server Side Rendering)
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService; // post controller는 postservice에 의존
    private final FileService fileService;
    private final CommentService commentService;

    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model) { // dispatcher suvlet이 model 전달
        log.info("/posts: GET, 목록 요청!");
        List<Post> allposts = postService.getAllPosts();

        model.addAttribute("posts", allposts);
        model.addAttribute("pageTitle", "블로그에 오신 것을 환영합니다.");
        // Model: 자바 데이터를 뷰(사용자에게 응답할 페이지)로 전달하는 용도로 사용하는 객체(스프링 제공)
        // Model 여러 객체 받을 수 있음
        // dispatcher suvelet이 두 모델에 담긴 내용 보내주는 것
        return "posts/list";

    }

    @GetMapping("/new")
    public String newPostForm(Model model) {
        model.addAttribute("pageTitle", "✍️ 새 글 작성");
        model.addAttribute("categories", Category.values()); // values-> category 배열 전달됨 - 상수 나열되어 있음

        return "posts/form";
    }

    @PostMapping
    public String create(PostRequest postRequest,
                         @RequestParam(value = "thumbnail", required = false) MultipartFile file, // 이미지 파일 한꺼번에 포함시키지 않고 dto에 따로 받아볼 것
                         Model model) { // entity는 db쪽과 연관이 깊기 때문에 entity 전달하는 건 위험할 수 있음
        // @RequestParam String title, @RequestParam String content,....로 낱개로 하나씩 받을 수 있지만 많은 값을 받기에는 부적절(한두개 정도는 괜찮다)
        // post 객체 전송된 데이터 이름, 필드 모두 가지고 있기 때문에 post란 객체로 포장해 전달받겠다 log.info - @Setter 필요 - null 들어갈 수 있음, null pointer exception 발생 문제
        log.info("/posts: Post, 전달된 값: {}", postRequest);

        // 파일 얿로드 처리
        if (file != null && !file.isEmpty()) {
            String fileName = fileService.saveFile(file);
            postRequest = postRequest.withThumbnailPath(fileName);
        }



        Post post=postService.createPost(postRequest);

        // return "posts/list";
        // 글 등록 완료된 이후 list.html 은 Model(Pagetitle, Posts:List) 넘어온다는 가정 하에 작성된 파일
        // posts 데이터가 모델에 담겨져 넘어와야 하는데 글 등록 완료된 후 넘어올 때는 posts 데이터 없다는 문제 -> 넘겨줘야 함
        // 글 등록 완료된 이후 요청이 다시 들어오게 유도할 것
        return "redirect:/posts";
        // redirect: 재요청
        // redirect://posts: 응답을 클라이언트로 내보낸 후 자동 재요청으로 /posts 요청이 들어오도록 유도해달라
        // 내가 가진 파일이 아닌 경로로 응답이 나가고 이 경로로 자동 재요청 들어오도록 요구
    }
    // client -post->
    // <-redirect-
    // -/posts:Get->
    // <-list.html-
    // 사용자는 글 등록 완료 후 글 등록이 완료된 글까지 포함된 화면을 볼 수 있음
    // 이때 사용되는 것이 redirect

    @GetMapping("/{id}")
    public String detail (@PathVariable Long id, Model model){ // url에 작성되어 있는 특정 값을 얻어오기 위해 @PathVariable
        PostResponse resDto = postService.getPostById(id);// 기본적으로 controller가 service에 의존
// return post; 가능은 하나, 요청을 받을 때도 DTO를 받았기 때문에 응답을 받을 때도 DTO로 받는 것이 좋음
        // 글 상세 보여주는 페이지에서 모든 정보가 필요하지 않을 수 있음
        // entity 원본을 전달하는 것은 지양하자‼️
        // 각각의 응답에 맞는 것을 리턴해주자
        // 댓글 추가로 가져와야 함
        List<CommentResponse> dtoList = commentService.getCommentsByPostId(id);
        model.addAttribute("post", resDto);
        model.addAttribute("pageTitle", resDto.title()); // 레코드에서 제공하는 getter는 "get(title, content, author...)" "get" 붙지 않음
        // 필드명과 동일

        model.addAttribute("comments", dtoList); // 댓글 없다 가정하고 빈 리스트 생성
        return "posts/detail";
    }

    // 검색 기능
    @GetMapping("/search")
    public String search(@RequestParam(required = false) String keyword,
                         @RequestParam(required = false) Category category,
                         @RequestParam(required = false, defaultValue = "latest") String sort,
                         Model model) {

        // @RequestParam - boolean required() default true; => 값이 전달되지 않으면 무조건 error
        // 카테고리만 올 수도 있고, sort만 올 수도 있음
        // "(required = false)" - 에러 방지시켜주자 ‼️
        // "required = false, defaultValue = "latest")" 만일 전달되지 않았다면 기본값 설정까지도 가능

        List<PostResponse> dtoList=postService.searchPost(keyword, category, sort);

        model.addAttribute("posts", dtoList);
        model.addAttribute("pageTitle", "🔎 검색 결과");
        // 화면단에 사용자가 선택한 여러가지 조건들을 렌더링 과정에 표시하기 위해 추가 정보를 model에 담아 내려줌
        model.addAttribute("keyword", keyword);
        model.addAttribute("category", category);
        model.addAttribute("sort", sort);

        return "posts/list";

    }

    /// ///////////////////////////////////////////////////////////////////////////////////////////////////////
    ///
    /// @return

    // 댓글 관련 엔드포인트(컨트롤러가 접근할 수 있는 지점, 데이터를 요청하고 받을 수 있는 지점)
    // == 댓글 관련 요청을 받을 수 있는 매핑 메소드 작성할 것
    // 예: creat는 게시글 작성의 end point
    // 여러가지 end point => ApplicationProgramInterface
    // 인터페이스는 형태, 규칙, 틀
    // 웹에서 서로 다른 데이터 통신 교환 규칙 => web api
    // -> controller

    // 댓글 작성
    @PostMapping("/{id}/comments")
    public String addComment(@PathVariable Long id,
                             @RequestParam String author, // dto로 작성할 수 있지만 값이 적을 경우는 @RequestParam 사용
                             @RequestParam String content){
        log.info("POST /posts{}/comments - author: {}", id, author);
        // sout(출력 기능 밖에 없음) 대신 log.info(로그 레벨, 어느 클래스에서 출력되었는지, 출력 시간, 로그만 모아 파일로 출력 가능) 사용
        CommentResponse resDto = commentService.createComment(id, author, content);
        // 댓글 작성이 완료된 후 return "posts/detail" 게시물 상세 보기에 대한 내용까지 포함시켜야 하기 때문에 게시물 조회 로직을 새로 쓸 필요 없어 게시물 상세 보기 요청이 다시 들어오도록 함(detail 메서드)
        return "redirect:/posts/"+id;
    }

    // 댓글 삭제

}
