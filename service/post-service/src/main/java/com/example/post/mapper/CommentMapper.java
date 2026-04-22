package com.example.post.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.post.entity.Comment;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 
 * @since 2024-04-01
 */
public interface CommentMapper extends BaseMapper<Comment> {

    /**
     * 分页查询帖子的评论
     */
    IPage<Comment> getCommentsByPostId(Page<Comment> page, @Param("postId") Long postId);

    /**
     * 查询用户发布的评论
     */
    IPage<Comment> getUserComments(Page<Comment> page, @Param("userId") Long userId);

    /**
     * 删除评论（软删除）
     */
    int deleteComment(@Param("commentId") Long commentId);
}
