package com.example.court.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("court")
public class Court implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "court_id", type = IdType.AUTO)
    private Long courtId;

    @TableField("court_name")
    private String courtName;

    @TableField("court_type")
    private String courtType;

    @TableField("location")
    private String location;

    @TableField("device_id")
    private String deviceId;

    @TableField("status")
    private Integer status;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;

    @TableLogic
    @TableField("deleted")
    private Integer deleted;
}
