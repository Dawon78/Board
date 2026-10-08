package com.dawon.board.post.controller;

import com.dawon.board.post.dto.LikeResponse;
import com.dawon.board.post.dto.PostForm;
import com.dawon.board.post.service.PostLikeService;
import com.dawon.board.post.service.PostService;
import com.dawon.board.security.LoginMember;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final PostLikeService postLikeService;

    @GetMapping
    public String list(@RequestParam(defaultValue = "1") int page, Model model) {
        int totalPages = postService.totalPages();
        int currentPage = Math.min(Math.max(page, 1), totalPages);
        model.addAttribute("posts", postService.findPage(currentPage));
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        return "post/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id,
                         @AuthenticationPrincipal LoginMember loginMember,
                         Model model) {
        model.addAttribute("post", postService.findDetail(id));
        model.addAttribute("liked", loginMember != null && postLikeService.isLiked(id, loginMember.getId()));
        return "post/detail";
    }

    @GetMapping("/new")
    public String createForm(@ModelAttribute PostForm postForm) {
        return "post/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute PostForm postForm,
                         BindingResult bindingResult,
                         @AuthenticationPrincipal LoginMember loginMember) {
        if (bindingResult.hasErrors()) {
            return "post/form";
        }
        Long id = postService.create(postForm, loginMember.getId());
        return "redirect:/posts/" + id;
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id,
                           @AuthenticationPrincipal LoginMember loginMember,
                           Model model) {
        model.addAttribute("postForm", postService.getEditForm(id, loginMember.getId()));
        model.addAttribute("postId", id);
        return "post/form";
    }

    @PostMapping("/{id}/edit")
    public String edit(@PathVariable Long id,
                       @Valid @ModelAttribute PostForm postForm,
                       BindingResult bindingResult,
                       @AuthenticationPrincipal LoginMember loginMember,
                       Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("postId", id);
            return "post/form";
        }
        postService.update(id, postForm, loginMember.getId());
        return "redirect:/posts/" + id;
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id,
                         @AuthenticationPrincipal LoginMember loginMember) {
        postService.delete(id, loginMember.getId());
        return "redirect:/posts";
    }

    @PostMapping("/{id}/like")
    @ResponseBody
    public LikeResponse like(@PathVariable Long id,
                             @AuthenticationPrincipal LoginMember loginMember) {
        return postLikeService.toggle(id, loginMember.getId());
    }
}