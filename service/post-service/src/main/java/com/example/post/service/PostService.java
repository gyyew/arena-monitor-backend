package com.example.post.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.post.entity.Post;

import java.util.List;

public interface PostService {

    Post createPost(Long userId, String title, String content, String sportType, String imageUrls);

    Post updatePost(Long postId, Long userId, String title, String content, String sportType, String imageUrls);

    boolean deletePost(Long postId, Long userId);

    Post getPostById(Long postId);

    IPage<Post> getPostList(int page, int size, String sportType, String keyword, Integer auditStatus);

    IPage<Post> getUserPosts(Long userId, int page, int size);

    boolean auditPost(Long postId, Integer auditStatus, String rejectReason);
}
