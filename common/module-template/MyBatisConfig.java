package com.example.${moduleName}.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis-Plus Configuration
 *
 * This configuration class enables MyBatis-Plus mapper scanning for the service.
 * The @MapperScan annotation automatically registers all mapper interfaces in the
 * specified package as Spring beans, eliminating the need for individual @Mapper
 * annotations on each mapper interface.
 *
 * Usage:
 *   - Replace ${moduleName} with your module's package name (e.g., "user", "post", "court")
 *   - Ensure this class is in the config package under your module's main package
 */
@Configuration
@MapperScan("com.example.${moduleName}.mapper")
public class MyBatisConfig {
    // MyBatis-Plus additional configuration can be added here if needed
    // For example: Interceptors, TypeHandlers, GlobalConfiguration, etc.
}
