package com.example.post.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.post.entity.Message;

import java.util.List;

public interface MessageService {

    Message sendMessage(Long sendUserId, Long receiveUserId, String content);

    IPage<Message> getUserMessages(Long userId, Long otherUserId, int page, int size);

    int getUnreadMessageCount(Long userId);

    boolean markMessageAsRead(Long messageId);

    boolean markAllMessagesAsRead(Long userId, Long otherUserId);

    boolean deleteMessage(Long messageId);

    List<Message> getConversationList(Long userId);
}
