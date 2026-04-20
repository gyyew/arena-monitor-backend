#!/bin/bash

# 项目初始化脚本 - 一键创建符合标准的后端模块
# 使用方法: ./scripts/init-module.sh <module-name> <port> <database-name>

set -e

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

# 检查参数
if [ $# -lt 3 ]; then
    echo -e "${RED}用法: $0 <module-name> <port> <database-name>${NC}"
    echo -e "${YELLOW}示例: $0 order 8084 court_monitor${NC}"
    exit 1
fi

MODULE_NAME=$1
PORT=$2
DATABASE=$3
SERVICE_DIR="service/${MODULE_NAME}-service"
PACKAGE_NAME="com.example.${MODULE_NAME}"

# 检查模块是否已存在
if [ -d "$SERVICE_DIR" ]; then
    echo -e "${RED}错误: 模块 $SERVICE_DIR 已存在${NC}"
    exit 1
fi

echo -e "${GREEN}开始创建模块: ${MODULE_NAME}-service${NC}"

# 创建目录结构
mkdir -p "$SERVICE_DIR/src/main/java/com/example/${MODULE_NAME}"
mkdir -p "$SERVICE_DIR/src/main/resources/mapper"
mkdir -p "$SERVICE_DIR/src/test/java/com/example/${MODULE_NAME}"

# 复制模板文件
TEMPLATE_DIR="common/module-template"

# 创建 pom.xml
cat > "$SERVICE_DIR/pom.xml" << EOF
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>com.example</groupId>
        <artifactId>court-monitor</artifactId>
        <version>1.0.0</version>
    </parent>

    <artifactId>${MODULE_NAME}-service</artifactId>
    <version>1.0.0</version>
    <packaging>jar</packaging>

    <name>${MODULE_NAME}-service</name>
    <description>${MODULE_NAME} Service</description>

    <dependencies>
        <dependency>
            <groupId>com.example</groupId>
            <artifactId>common</artifactId>
            <version>1.0.0</version>
        </dependency>
        
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-spring-boot3-starter</artifactId>
            <version>3.5.5</version>
        </dependency>
        
        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
            <scope>runtime</scope>
        </dependency>
        
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
        </dependency>
        
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>
        
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
EOF

# 创建 application.yml
cat > "$SERVICE_DIR/src/main/resources/application.yml" << EOF
server:
  port: ${PORT}

spring:
  application:
    name: ${MODULE_NAME}-service
  datasource:
    url: jdbc:mysql://localhost:3306/${DATABASE}?useSSL=false&useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
    username: root
    password: root
    driver-class-name: com.mysql.cj.jdbc.Driver
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848
      config:
        enabled: false

mybatis-plus:
  mapper-locations: classpath:mapper/**/*.xml
  type-aliases-package: com.example.${MODULE_NAME}.entity
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
EOF

# 创建 MyBatisConfig.java
cat > "$SERVICE_DIR/src/main/java/com/example/${MODULE_NAME}/config/MyBatisConfig.java" << EOF
package com.example.${MODULE_NAME}.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.example.${MODULE_NAME}.mapper")
public class MyBatisConfig {
}
EOF

# 创建启动类
cat > "$SERVICE_DIR/src/main/java/com/example/${MODULE_NAME}/${MODULE_NAME^}ServiceApplication.java" << EOF
package com.example.${MODULE_NAME};

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ${MODULE_NAME^}ServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(${MODULE_NAME^}ServiceApplication.class, args);
    }
}
EOF

echo -e "${GREEN}✓ 目录结构创建完成${NC}"
echo -e "${GREEN}✓ pom.xml 创建完成${NC}"
echo -e "${GREEN}✓ application.yml 创建完成${NC}"
echo -e "${GREEN}✓ MyBatisConfig.java 创建完成${NC}"
echo -e "${GREEN}✓ 启动类创建完成${NC}"

# 添加到父 pom
if ! grep -q "<module>${MODULE_NAME}-service</module>" pom.xml; then
    echo "  <module>${MODULE_NAME}-service</module>" >> pom.xml.tmp
    echo -e "${GREEN}✓ 添加到父 pom.xml${NC}"
fi

# 运行配置检查
echo ""
echo -e "${YELLOW}运行配置检查...${NC}"
./scripts/check-mapper-scan.sh

echo ""
echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}模块创建成功!${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""
echo -e "${YELLOW}下一步操作:${NC}"
echo -e "  1. 在 Nacos 中创建配置文件: ${MODULE_NAME}-service.yml"
echo -e "  2. 创建数据库: CREATE DATABASE ${DATABASE}"
echo -e "  3. 运行模块: mvn spring-boot:run -pl service/${MODULE_NAME}-service"
echo ""
