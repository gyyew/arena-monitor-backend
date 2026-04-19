package com.example.post.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.post.entity.Post;
import com.example.post.mapper.PostMapper;
import com.example.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostMapper postMapper;

    @Override
    public Post createPost(Integer userId, String title, String content, String sportType, String imageUrls) {
        Post post = new Post();
        post.setUserId(userId);
        post.setTitle(title);
        post.setContent(content);
        post.setSportType(sportType);
        post.setImageUrls(imageUrls);
        post.setAuditStatus(0); // 0-待审核
        
        postMapper.insert(post);
        return post;
    }

    @Override
    public Post updatePost(Integer postId, Integer userId, String title, String content, String sportType, String imageUrls) {
        Post post = postMapper.selectById(postId);
        if (post == null) {
            throw new RuntimeException("Post not found");
        }
        
        if (!post.getUserId().equals(userId)) {
            throw new RuntimeException("No permission to update this post");
        }
        
        if (post.getAuditStatus() != 0) {
            throw new RuntimeException("Cannot update post that has been audited");
        }
        
        post.setTitle(title);
        post.setContent(content);
        post.setSportType(sportType);
        post.setImageUrls(imageUrls);
        
        postMapper.updateById(post);
        return post;
    }

    @Override
    public boolean deletePost(Integer postId, Integer userId) {
        Post post = postMapper.selectById(postId);
        if (post == null) {
            throw new RuntimeException("Post not found");
        }
        
        if (!post.getUserId().equals(userId)) {
            throw new RuntimeException("No permission to delete this post");
        }
        
        return postMapper.deleteById(postId) > 0;
    }

    @Override
    public Post getPostById(Integer postId) {
        return postMapper.selectById(postId);
    }

    @Override
    public IPage<Post> getPostList(int page, int size, String sportType, String keyword, Integer auditStatus) {
        Page<Post> pageInfo = new Page<>(page, size);
        return postMapper.getPostList(pageInfo, sportType, keyword, auditStatus);
    }

    @Override
    public IPage<Post> getUserPosts(Integer userId, int page, int size) {
        Page<Post> pageInfo = new Page<>(page, size);
        return postMapper.getUserPosts(pageInfo, userId);
    }

    @Override
    public boolean auditPost(Integer postId, Integer auditStatus, String rejectReason) {
        int result = postMapper.auditPost(postId, auditStatus, rejectReason);
        return result > 0;
    }

    @Override
    public boolean likePost(Integer postId, Integer userId) {
        Post post = postMapper.selectById(postId);
        if (post == null) {
            throw new RuntimeException("Post not found");
        }
        
        // Check if already liked by this user
        int liked = postMapper.checkUserLike(postId, userId);
        if (liked > 0) {
            throw new RuntimeException("Already liked this post");
        }
        
        // Add like record
        postMapper.insertUserLike(postId, userId);
        
        // Increment post like count
        return postMapper.incrementLikeCount(postId) > 0;
    }
}
