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
                                 @Param("userId") Long userId, 
                                 @Param("otherUserId") Long otherUserId);

    /**
     * 获取用户的未读消息数量
     */
    int getUnreadMessageCount(@Param("userId") Long userId);

    /**
     * 标记消息为已读
     */
    int markMessageAsRead(@Param("messageId") Long messageId);

    /**
     * 标记与某用户的所有消息为已读
     */
    int markAllMessagesAsRead(@Param("userId") Long userId, @Param("otherUserId") Long otherUserId);

    /**
     * 删除私信（软删除）
     */
    int deleteMessage(@Param("messageId") Long messageId);

    /**
     * 获取用户的对话列表
     */
    List<Message> getConversationList(@Param("userId") Long userId);
}
