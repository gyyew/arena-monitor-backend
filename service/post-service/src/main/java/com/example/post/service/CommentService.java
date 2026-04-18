package com.example.post.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.post.entity.Comment;

public interface CommentService {

    Comment createComment(Integer postId, Integer userId, String content);

    boolean deleteComment(Integer commentId, Integer userId);

    IPage<Comment> getCommentsByPostId(Integer postId, int page, int size);

    IPage<Comment> getUserComments(Integer userId, int page, int size);
}
