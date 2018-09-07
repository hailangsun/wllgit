package com.wll.demo.testplus.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.wll.demo.testplus.mapper")
public class MybatisPlusConfig {

}
