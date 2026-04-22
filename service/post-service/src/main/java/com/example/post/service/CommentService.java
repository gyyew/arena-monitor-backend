package com.example.post.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.post.entity.Comment;

public interface CommentService {

    Comment createComment(Long postId, Long userId, String content);

    boolean deleteComment(Long commentId, Long userId);

    IPage<Comment> getCommentsByPostId(Long postId, int page, int size);

    IPage<Comment> getUserComments(Long userId, int page, int size);
}
