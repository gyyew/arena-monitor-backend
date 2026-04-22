package com.example.post.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.common.api.Result;
import com.example.post.entity.Comment;
import com.example.post.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
@Validated
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public Result<Comment> createComment(
            @RequestParam("postId") Long postId,
            @RequestParam("content") String content) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || authentication.getPrincipal() == null) {
                return Result.error(401, "Not authenticated");
            }
            Long userId = (Long) authentication.getPrincipal();
            
            Comment comment = commentService.createComment(postId, userId, content);
            return Result.success(comment);
        } catch (RuntimeException e) {
            return Result.error(400, e.getMessage());
        }
    }

    @DeleteMapping("/{commentId}")
    public Result<String> deleteComment(@PathVariable("commentId") Long commentId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || authentication.getPrincipal() == null) {
                return Result.error(401, "Not authenticated");
            }
            Long userId = (Long) authentication.getPrincipal();
            
            boolean success = commentService.deleteComment(commentId, userId);
            if (success) {
                return Result.success("Comment deleted successfully");
            } else {
                return Result.error(404, "Comment not found");
            }
        } catch (RuntimeException e) {
            return Result.error(400, e.getMessage());
        }
    }

    @GetMapping("/post/{postId}")
    public Result<IPage<Comment>> getCommentsByPostId(
            @PathVariable("postId") Long postId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        try {
            IPage<Comment> comments = commentService.getCommentsByPostId(postId, page, size);
            return Result.success(comments);
        } catch (RuntimeException e) {
            return Result.error(400, e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public Result<IPage<Comment>> getUserComments(
            @PathVariable("userId") Long userId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        try {
            IPage<Comment> comments = commentService.getUserComments(userId, page, size);
            return Result.success(comments);
        } catch (RuntimeException e) {
            return Result.error(400, e.getMessage());
        }
    }
}
