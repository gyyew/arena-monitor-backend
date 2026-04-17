-- Database for user service
CREATE DATABASE IF NOT EXISTS micro_user;
USE micro_user;

-- User table
CREATE TABLE IF NOT EXISTS `user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Primary key',
    `username` VARCHAR(50) NOT NULL COMMENT 'Username, unique',
    `password` VARCHAR(255) NOT NULL COMMENT 'Encrypted password',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT 'Phone number',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT 'Create time',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update time',
    `deleted` TINYINT DEFAULT 0 COMMENT 'Soft delete flag: 0=active, 1=deleted',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='User table';
