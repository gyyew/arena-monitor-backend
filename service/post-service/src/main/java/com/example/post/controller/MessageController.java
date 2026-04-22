package com.example.post.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.common.api.Result;
import com.example.post.entity.Message;
import com.example.post.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/messages")
@RequiredArgsConstructor
@Validated
public class MessageController {

    private final MessageService messageService;

    @PostMapping
    public Result<Message> sendMessage(
            @RequestParam("receiveUserId") Long receiveUserId,
            @RequestParam("content") String content) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || authentication.getPrincipal() == null) {
                return Result.error(401, "Not authenticated");
            }
            Long sendUserId = (Long) authentication.getPrincipal();
            
            Message message = messageService.sendMessage(sendUserId, receiveUserId, content);
            return Result.success(message);
        } catch (RuntimeException e) {
            return Result.error(400, e.getMessage());
        }
    }

    @GetMapping
    public Result<IPage<Message>> getUserMessages(
            @RequestParam("otherUserId") Long otherUserId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "20") int size) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || authentication.getPrincipal() == null) {
                return Result.error(401, "Not authenticated");
            }
            Long userId = (Long) authentication.getPrincipal();
            
            IPage<Message> messages = messageService.getUserMessages(userId, otherUserId, page, size);
            // 标记消息为已读
            messageService.markAllMessagesAsRead(userId, otherUserId);
            return Result.success(messages);
        } catch (RuntimeException e) {
            return Result.error(400, e.getMessage());
        }
    }

    @GetMapping("/unread/count")
    public Result<Integer> getUnreadMessageCount() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || authentication.getPrincipal() == null) {
                return Result.error(401, "Not authenticated");
            }
            Long userId = (Long) authentication.getPrincipal();
            
            int count = messageService.getUnreadMessageCount(userId);
            return Result.success(count);
        } catch (RuntimeException e) {
            return Result.error(400, e.getMessage());
        }
    }

    @PutMapping("/{messageId}/read")
    public Result<String> markMessageAsRead(@PathVariable("messageId") Long messageId) {
        try {
            boolean success = messageService.markMessageAsRead(messageId);
            if (success) {
                return Result.success("Message marked as read");
            } else {
                return Result.error(404, "Message not found");
            }
        } catch (RuntimeException e) {
            return Result.error(400, e.getMessage());
        }
    }

    @PutMapping("/read/all")
    public Result<String> markAllMessagesAsRead(@RequestParam("otherUserId") Long otherUserId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || authentication.getPrincipal() == null) {
                return Result.error(401, "Not authenticated");
            }
            Long userId = (Long) authentication.getPrincipal();
            
            boolean success = messageService.markAllMessagesAsRead(userId, otherUserId);
            if (success) {
                return Result.success("All messages marked as read");
            } else {
                return Result.error(404, "Messages not found");
            }
        } catch (RuntimeException e) {
            return Result.error(400, e.getMessage());
        }
    }

    @DeleteMapping("/{messageId}")
    public Result<String> deleteMessage(@PathVariable("messageId") Long messageId) {
        try {
            boolean success = messageService.deleteMessage(messageId);
            if (success) {
                return Result.success("Message deleted successfully");
            } else {
                return Result.error(404, "Message not found");
            }
        } catch (RuntimeException e) {
            return Result.error(400, e.getMessage());
        }
    }

    @GetMapping("/conversations")
    public Result<List<Message>> getConversationList() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || authentication.getPrincipal() == null) {
                return Result.error(401, "Not authenticated");
            }
            Long userId = (Long) authentication.getPrincipal();
            
            List<Message> conversations = messageService.getConversationList(userId);
            return Result.success(conversations);
        } catch (RuntimeException e) {
            return Result.error(400, e.getMessage());
        }
    }
}
