package com.example.post.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.post.entity.Post;

import java.util.List;

public interface PostService {

    Post createPost(Integer userId, String title, String content, String sportType, String imageUrls);

    Post updatePost(Integer postId, Integer userId, String title, String content, String sportType, String imageUrls);

    boolean deletePost(Integer postId, Integer userId);

    Post getPostById(Integer postId);

    IPage<Post> getPostList(int page, int size, String sportType, String keyword, Integer auditStatus);

    IPage<Post> getUserPosts(Integer userId, int page, int size);

    boolean auditPost(Integer postId, Integer auditStatus, String rejectReason);
}
