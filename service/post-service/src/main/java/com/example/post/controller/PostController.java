package com.example.post.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.post.common.Result;
import com.example.post.entity.Post;
import com.example.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@Validated
public class PostController {

    private final PostService postService;

    @PostMapping
    public Result<Post> createPost(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("sportType") String sportType,
            @RequestParam(value = "imageUrls", required = false) String imageUrls) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || authentication.getPrincipal() == null) {
                return Result.error(401, "Not authenticated");
            }
            Integer userId = (Integer) authentication.getPrincipal();
            
            Post post = postService.createPost(userId, title, content, sportType, imageUrls);
            return Result.success(post);
        } catch (RuntimeException e) {
            return Result.error(400, e.getMessage());
        }
    }

    @PutMapping("/{postId}")
    public Result<Post> updatePost(
            @PathVariable("postId") Integer postId,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("sportType") String sportType,
            @RequestParam(value = "imageUrls", required = false) String imageUrls) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || authentication.getPrincipal() == null) {
                return Result.error(401, "Not authenticated");
            }
            Integer userId = (Integer) authentication.getPrincipal();
            
            Post post = postService.updatePost(postId, userId, title, content, sportType, imageUrls);
            return Result.success(post);
        } catch (RuntimeException e) {
            return Result.error(400, e.getMessage());
        }
    }

    @DeleteMapping("/{postId}")
    public Result<String> deletePost(@PathVariable("postId") Integer postId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || authentication.getPrincipal() == null) {
                return Result.error(401, "Not authenticated");
            }
            Integer userId = (Integer) authentication.getPrincipal();
            
            boolean success = postService.deletePost(postId, userId);
            if (success) {
                return Result.success("Post deleted successfully");
            } else {
                return Result.error(404, "Post not found");
            }
        } catch (RuntimeException e) {
            return Result.error(400, e.getMessage());
        }
    }

    @GetMapping("/{postId}")
    public Result<Post> getPostById(@PathVariable("postId") Integer postId) {
        try {
            Post post = postService.getPostById(postId);
            if (post == null) {
                return Result.error(404, "Post not found");
            }
            return Result.success(post);
        } catch (RuntimeException e) {
            return Result.error(400, e.getMessage());
        }
    }

    @GetMapping
    public Result<IPage<Post>> getPostList(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sportType", required = false) String sportType,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "auditStatus", required = false) Integer auditStatus) {
        try {
            IPage<Post> postList = postService.getPostList(page, size, sportType, keyword, auditStatus);
            return Result.success(postList);
        } catch (RuntimeException e) {
            return Result.error(400, e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public Result<IPage<Post>> getUserPosts(
            @PathVariable("userId") Integer userId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        try {
            IPage<Post> postList = postService.getUserPosts(userId, page, size);
            return Result.success(postList);
        } catch (RuntimeException e) {
            return Result.error(400, e.getMessage());
        }
    }

    @PutMapping("/admin/audit/{postId}")
    public Result<String> auditPost(
            @PathVariable("postId") Integer postId,
            @RequestParam("auditStatus") Integer auditStatus,
            @RequestParam(value = "rejectReason", required = false) String rejectReason) {
        try {
            boolean success = postService.auditPost(postId, auditStatus, rejectReason);
            if (success) {
                return Result.success("Post audited successfully");
            } else {
                return Result.error(404, "Post not found");
            }
        } catch (RuntimeException e) {
            return Result.error(400, e.getMessage());
        }
    }
}
