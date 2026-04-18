package com.example.post.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.post.entity.Message;
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
public interface MessageMapper extends BaseMapper<Message> {

    /**
     * 分页查询用户的私信
     */
    IPage<Message> getUserMessages(Page<Message> page, 
                                 @Param("userId") Integer userId, 
                                 @Param("otherUserId") Integer otherUserId);

    /**
     * 获取用户的未读消息数量
     */
    int getUnreadMessageCount(@Param("userId") Integer userId);

    /**
     * 标记消息为已读
     */
    int markMessageAsRead(@Param("messageId") Integer messageId);

    /**
     * 标记与某用户的所有消息为已读
     */
    int markAllMessagesAsRead(@Param("userId") Integer userId, @Param("otherUserId") Integer otherUserId);

    /**
     * 删除私信（软删除）
     */
    int deleteMessage(@Param("messageId") Integer messageId);

    /**
     * 获取用户的对话列表
     */
    List<Message> getConversationList(@Param("userId") Integer userId);
}
