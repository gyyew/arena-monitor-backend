-- Court Management Database
CREATE DATABASE IF NOT EXISTS court_management;
USE court_management;

CREATE TABLE IF NOT EXISTS court (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Primary key',
    court_number INT NOT NULL COMMENT 'Court number (1, 2, 3...)',
    status VARCHAR(20) NOT NULL DEFAULT 'FREE' COMMENT 'FREE or OCCUPIED',
    image_url VARCHAR(500) COMMENT 'Latest image URL from device',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update time',
    deleted TINYINT DEFAULT 0 COMMENT 'Soft delete flag',
    PRIMARY KEY (id),
    UNIQUE KEY uk_court_number (court_number),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Badminton court';

-- Insert sample courts (4 courts)
INSERT INTO court (court_number, status) VALUES (1, 'FREE'), (2, 'FREE'), (3, 'FREE'), (4, 'FREE')
ON DUPLICATE KEY UPDATE update_time = CURRENT_TIMESTAMP;