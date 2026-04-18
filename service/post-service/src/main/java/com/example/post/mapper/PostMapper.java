package com.example.post.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.post.entity.Post;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 
 * @since 2024-04-01
 */
public interface PostMapper extends BaseMapper<Post> {

    /**
     * 分页查询帖子列表
     */
    IPage<Post> getPostList(Page<Post> page, 
                          @Param("sportType") String sportType, 
                          @Param("keyword") String keyword, 
                          @Param("auditStatus") Integer auditStatus);

    /**
     * 查询用户发布的帖子
     */
    IPage<Post> getUserPosts(Page<Post> page, @Param("userId") Integer userId);

    /**
     * 审核帖子
     */
    int auditPost(@Param("postId") Integer postId, 
                 @Param("auditStatus") Integer auditStatus, 
                 @Param("rejectReason") String rejectReason);
}
