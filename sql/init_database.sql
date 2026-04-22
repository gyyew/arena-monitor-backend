-- =====================================================
-- 统一数据库初始化脚本
-- 服务对应数据库：
--   user-service -> user_service
--   court-service -> court_service
--   post-service -> post_service
-- =====================================================

-- -----------------------------------------------------
-- 1. 用户数据库 (user-service)
-- -----------------------------------------------------
CREATE DATABASE IF NOT EXISTS user_service DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE user_service;

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
    `user_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名（登录账号）',
    `phone` VARCHAR(11) NOT NULL COMMENT '手机号',
    `password` VARCHAR(255) NOT NULL COMMENT '加密后的密码',
    `nickname` VARCHAR(50) DEFAULT NULL COMMENT '昵称',
    `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
    `sport_preference` VARCHAR(50) DEFAULT NULL COMMENT '运动偏好',
    `intro` VARCHAR(500) DEFAULT NULL COMMENT '个人简介',
    `role` TINYINT NOT NULL DEFAULT 0 COMMENT '角色: 0-普通用户, 1-管理员',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态: 0-正常, 1-禁用',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '软删除标志: 0-未删除, 1-已删除',
    PRIMARY KEY (`user_id`),
    UNIQUE KEY `uk_username` (`username`),
    UNIQUE KEY `uk_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 插入管理员账号 (密码: admin123)
INSERT INTO `user` (`username`, `phone`, `password`, `nickname`, `role`, `status`) VALUES
('admin', '13800138000', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '系统管理员', 1, 0);

-- 插入测试用户 (密码: 123456)
INSERT INTO `user` (`username`, `phone`, `password`, `nickname`, `sport_preference`, `intro`, `role`, `status`) VALUES
('user001', '13800138001', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '羽毛球爱好者', '羽毛球', '喜欢打羽毛球，每周固定打球', 0, 0),
('user002', '13800138002', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '篮球达人', '篮球', '篮球是我的最爱', 0, 0);


-- -----------------------------------------------------
-- 2. 场地服务数据库 (court-service)
-- -----------------------------------------------------
CREATE DATABASE IF NOT EXISTS court_service DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE court_service;

DROP TABLE IF EXISTS `court`;
CREATE TABLE `court` (
    `court_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '场地ID',
    `court_name` VARCHAR(50) NOT NULL COMMENT '场地名称',
    `court_type` VARCHAR(20) NOT NULL COMMENT '场地类型: 羽毛球、篮球、乒乓球等',
    `location` VARCHAR(100) DEFAULT NULL COMMENT '场地位置',
    `device_id` VARCHAR(50) DEFAULT NULL COMMENT '设备ID',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态: 0-正常, 1-维护中',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '软删除标志: 0-未删除, 1-已删除',
    PRIMARY KEY (`court_id`),
    UNIQUE KEY `uk_device_id` (`device_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='场地表';

DROP TABLE IF EXISTS `court_monitor`;
CREATE TABLE `court_monitor` (
    `monitor_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '监测ID',
    `court_id` BIGINT NOT NULL COMMENT '场地ID',
    `occupy_status` TINYINT NOT NULL DEFAULT 0 COMMENT '占用状态: 0-空闲, 1-占用',
    `people_count` INT NOT NULL DEFAULT 0 COMMENT '人数',
    `image_url` VARCHAR(255) NOT NULL COMMENT '现场图片URL',
    `identify_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '识别时间',
    `identify_status` TINYINT NOT NULL DEFAULT 0 COMMENT '识别状态: 0-正常, 1-异常',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`monitor_id`),
    KEY `idx_court_id` (`court_id`),
    KEY `idx_identify_time` (`identify_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='场地监测数据表';

-- 插入测试场地
INSERT INTO `court` (`court_name`, `court_type`, `location`, `device_id`) VALUES
('羽毛球馆1号场', '羽毛球', '体育馆1楼', 'device_001'),
('羽毛球馆2号场', '羽毛球', '体育馆1楼', 'device_002'),
('篮球场1号场', '篮球', '体育馆2楼', 'device_003'),
('篮球场2号场', '篮球', '体育馆2楼', 'device_004');

-- 插入测试监测数据
INSERT INTO `court_monitor` (`court_id`, `occupy_status`, `people_count`, `image_url`, `identify_status`) VALUES
(1, 1, 4, 'https://example.com/images/court1_1.jpg', 0),
(2, 0, 0, 'https://example.com/images/court2_1.jpg', 0),
(3, 1, 10, 'https://example.com/images/court3_1.jpg', 0),
(4, 0, 0, 'https://example.com/images/court4_1.jpg', 0);


-- -----------------------------------------------------
-- 3. 帖子服务数据库 (post-service)
-- -----------------------------------------------------
CREATE DATABASE IF NOT EXISTS post_service DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE post_service;

DROP TABLE IF EXISTS `post`;
CREATE TABLE `post` (
    `post_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '帖子ID',
    `user_id` BIGINT NOT NULL COMMENT '发布者用户ID',
    `title` VARCHAR(50) NOT NULL COMMENT '帖子标题',
    `content` TEXT NOT NULL COMMENT '帖子内容',
    `sport_type` VARCHAR(20) NOT NULL COMMENT '运动类型',
    `image_urls` VARCHAR(512) DEFAULT NULL COMMENT '图片URLs（JSON数组）',
    `audit_status` TINYINT NOT NULL DEFAULT 0 COMMENT '审核状态: 0-待审核, 1-审核通过, 2-审核驳回',
    `reject_reason` VARCHAR(200) DEFAULT NULL COMMENT '驳回原因',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '软删除标志: 0-未删除, 1-已删除',
    PRIMARY KEY (`post_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_audit_status` (`audit_status`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='帖子表';

DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment` (
    `comment_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '评论ID',
    `post_id` BIGINT NOT NULL COMMENT '帖子ID',
    `user_id` BIGINT NOT NULL COMMENT '评论者用户ID',
    `content` VARCHAR(200) NOT NULL COMMENT '评论内容',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态: 0-正常, 1-已删除',
    PRIMARY KEY (`comment_id`),
    KEY `idx_post_id` (`post_id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='评论表';

DROP TABLE IF EXISTS `message`;
CREATE TABLE `message` (
    `message_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '消息ID',
    `send_user_id` BIGINT NOT NULL COMMENT '发送者用户ID',
    `receive_user_id` BIGINT NOT NULL COMMENT '接收者用户ID',
    `content` VARCHAR(500) NOT NULL COMMENT '消息内容',
    `is_read` TINYINT NOT NULL DEFAULT 0 COMMENT '阅读状态: 0-未读, 1-已读',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态: 0-正常, 1-已删除',
    PRIMARY KEY (`message_id`),
    KEY `idx_send_user_id` (`send_user_id`),
    KEY `idx_receive_user_id` (`receive_user_id`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='私信表';

-- 插入测试帖子
INSERT INTO `post` (`user_id`, `title`, `content`, `sport_type`, `audit_status`) VALUES
(2, '周末羽毛球约起', '周末下午2点，羽毛球馆1号场，需要3人组队，有意者私信', '羽毛球', 1),
(3, '篮球爱好者集合', '每天晚上7点，篮球场1号场，欢迎加入', '篮球', 1);

-- 插入测试评论
INSERT INTO `comment` (`post_id`, `user_id`, `content`) VALUES
(1, 3, '我参加，水平一般可以吗？'),
(1, 2, '没问题，大家一起玩'),
(2, 2, '我也想参加，请问需要带什么装备？');

-- 插入测试私信
INSERT INTO `message` (`send_user_id`, `receive_user_id`, `content`, `is_read`) VALUES
(3, 2, '你好，我想参加周末的羽毛球活动', 0),
(2, 3, '好的，周末下午2点准时在羽毛球馆见', 1);
