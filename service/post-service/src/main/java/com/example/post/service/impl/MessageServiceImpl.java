package com.example.post.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.post.entity.Message;
import com.example.post.mapper.MessageMapper;
import com.example.post.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageMapper messageMapper;

    @Override
    public Message sendMessage(Long sendUserId, Long receiveUserId, String content) {
        Message message = new Message();
        message.setSendUserId(sendUserId);
        message.setReceiveUserId(receiveUserId);
        message.setContent(content);
        message.setIsRead(0); // 0-未读
        message.setStatus(0); // 0-正常
        
        messageMapper.insert(message);
        return message;
    }

    @Override
    public IPage<Message> getUserMessages(Long userId, Long otherUserId, int page, int size) {
        Page<Message> pageInfo = new Page<>(page, size);
        return messageMapper.getUserMessages(pageInfo, userId, otherUserId);
    }

    @Override
    public int getUnreadMessageCount(Long userId) {
        return messageMapper.getUnreadMessageCount(userId);
    }

    @Override
    public boolean markMessageAsRead(Long messageId) {
        int result = messageMapper.markMessageAsRead(messageId);
        return result > 0;
    }

    @Override
    public boolean markAllMessagesAsRead(Long userId, Long otherUserId) {
        int result = messageMapper.markAllMessagesAsRead(userId, otherUserId);
        return result > 0;
    }

    @Override
    public boolean deleteMessage(Long messageId) {
        int result = messageMapper.deleteMessage(messageId);
        return result > 0;
    }

    @Override
    public List<Message> getConversationList(Long userId) {
        return messageMapper.getConversationList(userId);
    }
}
