package com.springboot.blog.config;

import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Zhouzf
 * @date 2021/7/13/013 21:49
 */
@Configuration
public class MybatisConfig {

    @Bean
    ConfigurationCustomizer mybatisConfigurationCustomizer() {
        return configuration -> {
            configuration.setMapUnderscoreToCamelCase(true);
//            configuration.addMappers("db/mybatis/**.xml");
        };
    }

}
