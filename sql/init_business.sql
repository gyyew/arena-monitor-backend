-- 创建业务数据库
CREATE DATABASE IF NOT EXISTS court_monitor;

USE court_monitor;

-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `phone` varchar(11) NOT NULL,
  `password` varchar(32) NOT NULL,
  `nickname` varchar(20) NOT NULL,
  `avatar` varchar(255) DEFAULT NULL,
  `sport_preference` varchar(50) DEFAULT NULL,
  `intro` varchar(200) DEFAULT NULL,
  `role` int NOT NULL DEFAULT 0 COMMENT '0-普通用户，1-管理员',
  `status` int NOT NULL DEFAULT 0 COMMENT '0-正常，1-禁用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `uk_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 场地表
CREATE TABLE IF NOT EXISTS `court` (
  `court_id` int NOT NULL AUTO_INCREMENT,
  `court_name` varchar(50) NOT NULL,
  `court_type` varchar(20) NOT NULL,
  `location` varchar(100) DEFAULT NULL,
  `device_id` varchar(50) DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`court_id`),
  UNIQUE KEY `uk_device_id` (`device_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='场地表';

-- 场地监测数据表
CREATE TABLE IF NOT EXISTS `court_monitor` (
  `monitor_id` int NOT NULL AUTO_INCREMENT,
  `court_id` int NOT NULL,
  `occupy_status` int NOT NULL COMMENT '0-空闲，1-占用',
  `people_count` int NOT NULL,
  `image_url` varchar(255) NOT NULL,
  `identify_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `identify_status` int NOT NULL DEFAULT 0 COMMENT '0-识别正常，1-识别异常',
  PRIMARY KEY (`monitor_id`),
  KEY `idx_court_id` (`court_id`),
  CONSTRAINT `fk_court_monitor_court` FOREIGN KEY (`court_id`) REFERENCES `court` (`court_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='场地监测数据表';

-- 帖子表
CREATE TABLE IF NOT EXISTS `post` (
  `post_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `title` varchar(50) NOT NULL,
  `content` text NOT NULL,
  `sport_type` varchar(20) NOT NULL,
  `image_urls` varchar(512) DEFAULT NULL,
  `audit_status` int NOT NULL DEFAULT 0 COMMENT '0-待审核，1-审核通过，2-审核驳回',
  `reject_reason` varchar(50) DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`post_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_audit_status` (`audit_status`),
  CONSTRAINT `fk_post_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='帖子表';

-- 评论表
CREATE TABLE IF NOT EXISTS `comment` (
  `comment_id` int NOT NULL AUTO_INCREMENT,
  `post_id` int NOT NULL,
  `user_id` int NOT NULL,
  `content` varchar(100) NOT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `status` int NOT NULL DEFAULT 0 COMMENT '0-正常，1-已删除',
  PRIMARY KEY (`comment_id`),
  KEY `idx_post_id` (`post_id`),
  KEY `idx_user_id` (`user_id`),
  CONSTRAINT `fk_comment_post` FOREIGN KEY (`post_id`) REFERENCES `post` (`post_id`),
  CONSTRAINT `fk_comment_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论表';

-- 私信表
CREATE TABLE IF NOT EXISTS `message` (
  `message_id` int NOT NULL AUTO_INCREMENT,
  `send_user_id` int NOT NULL,
  `receive_user_id` int NOT NULL,
  `content` varchar(200) NOT NULL,
  `is_read` int NOT NULL DEFAULT 0 COMMENT '0-未读，1-已读',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `status` int NOT NULL DEFAULT 0 COMMENT '0-正常，1-已删除',
  PRIMARY KEY (`message_id`),
  KEY `idx_send_user_id` (`send_user_id`),
  KEY `idx_receive_user_id` (`receive_user_id`),
  CONSTRAINT `fk_message_send_user` FOREIGN KEY (`send_user_id`) REFERENCES `user` (`user_id`),
  CONSTRAINT `fk_message_receive_user` FOREIGN KEY (`receive_user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='私信表';

-- 插入测试数据
-- 插入管理员用户
INSERT INTO `user` (`phone`, `password`, `nickname`, `role`, `status`) VALUES
('13800138000', 'e10adc3949ba59abbe56e057f20f883e', '管理员', 1, 0);

-- 插入测试用户
INSERT INTO `user` (`phone`, `password`, `nickname`, `sport_preference`, `intro`, `role`, `status`) VALUES
('13800138001', 'e10adc3949ba59abbe56e057f20f883e', '羽毛球爱好者', '羽毛球', '喜欢打羽毛球，每周固定打球', 0, 0),
('13800138002', 'e10adc3949ba59abbe56e057f20f883e', '篮球达人', '篮球', '篮球是我的最爱', 0, 0);

-- 插入测试场地
INSERT INTO `court` (`court_name`, `court_type`, `location`, `device_id`) VALUES
('羽毛球馆1号场', '羽毛球', '体育馆1楼', 'device_001'),
('羽毛球馆2号场', '羽毛球', '体育馆1楼', 'device_002'),
('篮球场1号场', '篮球', '体育馆2楼', 'device_003'),
('篮球场2号场', '篮球', '体育馆2楼', 'device_004');

-- 插入测试场地监测数据
INSERT INTO `court_monitor` (`court_id`, `occupy_status`, `people_count`, `image_url`, `identify_status`) VALUES
(1, 1, 4, 'https://example.com/images/court1_1.jpg', 0),
(2, 0, 0, 'https://example.com/images/court2_1.jpg', 0),
(3, 1, 10, 'https://example.com/images/court3_1.jpg', 0),
(4, 0, 0, 'https://example.com/images/court4_1.jpg', 0);

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
