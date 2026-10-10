package com.dawon.board.comment.controller;

import com.dawon.board.comment.dto.CommentForm;
import com.dawon.board.comment.service.CommentLikeService;
import com.dawon.board.comment.service.CommentService;
import com.dawon.board.post.dto.LikeResponse;
import com.dawon.board.security.LoginMember;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final CommentLikeService commentLikeService;

    @PostMapping("/posts/{postId}/comments")
    public String create(@PathVariable Long postId,
                         @Valid @ModelAttribute CommentForm commentForm,
                         BindingResult bindingResult,
                         @AuthenticationPrincipal LoginMember loginMember,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("commentError", bindingResult.getFieldError().getDefaultMessage());
            return "redirect:/posts/" + postId + "#comments";
        }
        Long commentId = commentService.create(postId, commentForm, loginMember.getId());
        return "redirect:/posts/" + postId + "#comment-" + commentId;
    }

    @PostMapping("/posts/{postId}/comments/{id}/edit")
    public String edit(@PathVariable Long postId,
                       @PathVariable Long id,
                       @Valid @ModelAttribute CommentForm commentForm,
                       BindingResult bindingResult,
                       @AuthenticationPrincipal LoginMember loginMember,
                       RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("commentError", bindingResult.getFieldError().getDefaultMessage());
            return "redirect:/posts/" + postId + "#comment-" + id;
        }
        Long savedPostId = commentService.update(id, commentForm, loginMember.getId());
        return "redirect:/posts/" + savedPostId + "#comment-" + id;
    }

    @PostMapping("/posts/{postId}/comments/{id}/delete")
    public String delete(@PathVariable Long postId,
                         @PathVariable Long id,
                         @AuthenticationPrincipal LoginMember loginMember) {
        Long savedPostId = commentService.delete(id, loginMember.getId());
        return "redirect:/posts/" + savedPostId + "#comments";
    }

    @PostMapping("/comments/{id}/like")
    @ResponseBody
    public LikeResponse like(@PathVariable Long id,
                             @AuthenticationPrincipal LoginMember loginMember) {
        return commentLikeService.toggle(id, loginMember.getId());
    }
}