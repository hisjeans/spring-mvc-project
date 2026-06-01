package com.codeit.mvc.controller;

import com.codeit.mvc.domain.Category;
import com.codeit.mvc.domain.Post;
import com.codeit.mvc.dto.request.PostRequest;
import com.codeit.mvc.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller // 컨트롤러가 요청에 맞는 뷰 페이지를 결정 (Server Side Rendering)
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService; // post controller는 postservice에 의존

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
    public String create(PostRequest postRequest, Model model) { // entity는 db쪽과 연관이 깊기 때문에 entity 전달하는 건 위험할 수 있음
        // @RequestParam String title, @RequestParam String content,....로 받을 수 있지만 많은 값을 받기에는 부적절(한두개 정도는 괜찮다)
        // post 객체 전송된 데이터 이름, 필드 모두 가지고 있기 때문에 post란 객체로 포장해 전달받겠다 log.info - @Setter 필요 - null 들어갈 수 있음, null pointer exception 발생 문제
        log.info("/posts: Post, 전달된 값: {}", postRequest);
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

    @GetMapping("/{id}")
    public String detail (@PathVariable Long id){
        postService.getPostById(id); // 기본적으로 controller가 service에 의존
    }

}
