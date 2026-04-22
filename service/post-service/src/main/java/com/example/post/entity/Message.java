package com.example.post.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("message")
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "message_id", type = IdType.AUTO)
    private Long messageId;

    @TableField("send_user_id")
    private Long sendUserId;

    @TableField("receive_user_id")
    private Long receiveUserId;

    @TableField("content")
    private String content;

    @TableField("is_read")
    private Integer isRead;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("status")
    private Integer status;
}