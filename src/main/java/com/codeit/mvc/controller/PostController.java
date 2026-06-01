package com.codeit.mvc.controller;

import com.codeit.mvc.domain.Category;
import com.codeit.mvc.domain.Post;
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
    public String list(Model model){ // dispatcher suvlet이 model 전달
        log.info("/posts: GET, 목록 요청!");
        List<Post> allposts=postService.getAllPosts();

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


}
