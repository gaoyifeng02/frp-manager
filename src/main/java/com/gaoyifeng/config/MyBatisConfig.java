package com.gaoyifeng.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis配置类
 */
@Configuration
@MapperScan("com.gaoyifeng.mapper")
public class MyBatisConfig {
    // MyBatis默认配置，使用注解方式配置
}
