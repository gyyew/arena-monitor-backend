package com.example.post.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.post.entity.Message;

import java.util.List;

public interface MessageService {

    Message sendMessage(Integer sendUserId, Integer receiveUserId, String content);

    IPage<Message> getUserMessages(Integer userId, Integer otherUserId, int page, int size);

    int getUnreadMessageCount(Integer userId);

    boolean markMessageAsRead(Integer messageId);

    boolean markAllMessagesAsRead(Integer userId, Integer otherUserId);

    boolean deleteMessage(Integer messageId);

    List<Message> getConversationList(Integer userId);
}
