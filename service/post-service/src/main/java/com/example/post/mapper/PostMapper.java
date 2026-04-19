package com.example.post.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.post.entity.Post;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper

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

    /**
     * Check if user already liked the post
     */
    @Select("SELECT COUNT(*) FROM post_like WHERE post_id = #{postId} AND user_id = #{userId}")
    int checkUserLike(@Param("postId") Integer postId, @Param("userId") Integer userId);

    /**
     * Insert user like record
     */
    @Insert("INSERT INTO post_like (post_id, user_id, create_time) VALUES (#{postId}, #{userId}, NOW())")
    int insertUserLike(@Param("postId") Integer postId, @Param("userId") Integer userId);

    /**
     * Increment post like count
     */
    @Update("UPDATE post SET like_count = like_count + 1 WHERE post_id = #{postId}")
    int incrementLikeCount(@Param("postId") Integer postId);
}
