package com.example.post.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.example.post.mapper")
public class MyBatisConfig {
}
