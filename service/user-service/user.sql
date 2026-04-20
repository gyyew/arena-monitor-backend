-- Database for user service
CREATE DATABASE IF NOT EXISTS micro_user;
USE micro_user;

-- User table
CREATE TABLE IF NOT EXISTS `user` (
    `user_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Primary key',
    `phone` VARCHAR(20) NOT NULL COMMENT 'Phone number, unique for login',
    `password` VARCHAR(255) NOT NULL COMMENT 'Encrypted password',
    `nickname` VARCHAR(50) DEFAULT NULL COMMENT 'User nickname',
    `avatar` VARCHAR(255) DEFAULT NULL COMMENT 'Avatar URL',
    `sport_preference` VARCHAR(50) DEFAULT NULL COMMENT 'Sport preference',
    `intro` VARCHAR(500) DEFAULT NULL COMMENT 'User introduction',
    `role` TINYINT DEFAULT 1 COMMENT 'Role: 1=user, 2=admin',
    `status` TINYINT DEFAULT 1 COMMENT 'Status: 1=active, 0=disabled',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT 'Create time',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update time',
    `deleted` TINYINT DEFAULT 0 COMMENT 'Soft delete flag: 0=active, 1=deleted',
    PRIMARY KEY (`user_id`),
    UNIQUE KEY `uk_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='User table';
