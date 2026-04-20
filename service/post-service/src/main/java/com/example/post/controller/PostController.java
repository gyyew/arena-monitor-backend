package com.example.post.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.post.common.Result;
import com.example.post.common.UserResult;
import com.example.post.dto.VerifyResponse;
import com.example.post.entity.Post;
import com.example.post.feign.UserServiceClient;
import com.example.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Post Controller
 * Handles CRUD operations for posts including create, update, delete, query, like, and audit
 * All write operations require authentication via Feign token verification
 */
@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@Validated
public class PostController {

    private final PostService postService;
    private final UserServiceClient userServiceClient;

    /**
     * Extract and validate user from Authorization header
     * @param authorization Authorization header with Bearer token
     * @return userId if token is valid, null otherwise
     */
    private Integer validateAndGetUserId(String authorization) {
        if (authorization == null || authorization.isEmpty()) {
            return null;
        }
        try {
            UserResult<VerifyResponse> result = userServiceClient.verifyToken(authorization);
            if (result != null && result.isSuccess() && result.getData() != null) {
                return result.getData().getUserId();
            }
        } catch (Exception e) {
            // Token verification failed
        }
        return null;
    }

    /**
     * Create a new post
     * Requires authentication via JWT token
     * @param title post title
     * @param content post content
     * @param sportType sport type
     * @param imageUrls optional image URLs
     * @return created post
     */
    @PostMapping
    public Result<Post> createPost(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("sportType") String sportType,
            @RequestParam(value = "imageUrls", required = false) String imageUrls) {
        try {
            Integer userId = validateAndGetUserId(authorization);
            if (userId == null) {
                return Result.error(401, "Invalid or missing authentication token");
            }

            Post post = postService.createPost(userId, title, content, sportType, imageUrls);
            return Result.success(post);
        } catch (RuntimeException e) {
            return Result.error(400, e.getMessage());
        }
    }

    /**
     * Update an existing post
     * Only the post author can update their own post
     * @param postId post ID to update
     * @param title new title
     * @param content new content
     * @param sportType new sport type
     * @param imageUrls optional new image URLs
     * @return updated post
     */
    @PutMapping("/{postId}")
    public Result<Post> updatePost(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @PathVariable("postId") Integer postId,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("sportType") String sportType,
            @RequestParam(value = "imageUrls", required = false) String imageUrls) {
        try {
            Integer userId = validateAndGetUserId(authorization);
            if (userId == null) {
                return Result.error(401, "Invalid or missing authentication token");
            }

            Post post = postService.updatePost(postId, userId, title, content, sportType, imageUrls);
            return Result.success(post);
        } catch (RuntimeException e) {
            return Result.error(400, e.getMessage());
        }
    }

    /**
     * Delete a post
     * Only the post author can delete their own post
     * @param postId post ID to delete
     * @return success message
     */
    @DeleteMapping("/{postId}")
    public Result<String> deletePost(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @PathVariable("postId") Integer postId) {
        try {
            Integer userId = validateAndGetUserId(authorization);
            if (userId == null) {
                return Result.error(401, "Invalid or missing authentication token");
            }

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

    /**
     * Like a post - toggle like status
     * @param authorization Authorization header with Bearer token
     * @param postId post ID to like/unlike
     * @return result with like status message
     */
    @PostMapping("/{postId}/like")
    public Result<String> likePost(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @PathVariable("postId") Integer postId) {
        try {
            Integer userId = validateAndGetUserId(authorization);
            if (userId == null) {
                return Result.error(401, "Invalid or missing authentication token");
            }

            boolean success = postService.likePost(postId, userId);
            if (success) {
                return Result.success("Liked successfully");
            } else {
                return Result.success("Unliked successfully");
            }
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

    /**
     * Get audit list - posts pending for audit (auditStatus = 0)
     * @param page page number, default 1
     * @param size page size, default 10
     * @param sportType sport type filter, optional
     * @param keyword search keyword, optional
     * @return paginated post list waiting for audit
     */
    @GetMapping("/audit-list")
    public Result<IPage<Post>> getAuditList(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sportType", required = false) String sportType,
            @RequestParam(value = "keyword", required = false) String keyword) {
        try {
            // auditStatus = 0 means pending for audit
            IPage<Post> postList = postService.getPostList(page, size, sportType, keyword, 0);
            return Result.success(postList);
        } catch (RuntimeException e) {
            return Result.error(400, e.getMessage());
        }
    }
}
