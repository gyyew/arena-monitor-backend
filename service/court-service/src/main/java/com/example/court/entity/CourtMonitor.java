package com.example.court.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("court_monitor")
public class CourtMonitor implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "monitor_id", type = IdType.AUTO)
    private Integer monitorId;

    @TableField("court_id")
    private Integer courtId;

    @TableField("occupy_status")
    private Integer occupyStatus;

    @TableField("people_count")
    private Integer peopleCount;

    @TableField("image_url")
    private String imageUrl;

    @TableField("identify_time")
    private LocalDateTime identifyTime;

    @TableField("identify_status")
    private Integer identifyStatus;
}