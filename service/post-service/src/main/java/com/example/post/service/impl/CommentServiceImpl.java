package com.example.post.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.post.entity.Comment;
import com.example.post.mapper.CommentMapper;
import com.example.post.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentMapper commentMapper;

    @Override
    public Comment createComment(Long postId, Long userId, String content) {
        Comment comment = new Comment();
        comment.setPostId(postId);
        comment.setUserId(userId);
        comment.setContent(content);
        comment.setStatus(0); // 0-正常
        
        commentMapper.insert(comment);
        return comment;
    }

    @Override
    public boolean deleteComment(Long commentId, Long userId) {
        Comment comment = commentMapper.selectById(commentId);
        if (comment == null) {
            throw new RuntimeException("Comment not found");
        }
        
        if (!comment.getUserId().equals(userId)) {
            throw new RuntimeException("No permission to delete this comment");
        }
        
        int result = commentMapper.deleteComment(commentId);
        return result > 0;
    }

    @Override
    public IPage<Comment> getCommentsByPostId(Long postId, int page, int size) {
        Page<Comment> pageInfo = new Page<>(page, size);
        return commentMapper.getCommentsByPostId(pageInfo, postId);
    }

    @Override
    public IPage<Comment> getUserComments(Long userId, int page, int size) {
        Page<Comment> pageInfo = new Page<>(page, size);
        return commentMapper.getUserComments(pageInfo, userId);
    }
}
