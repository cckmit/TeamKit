package org.team4u.test.spring;

import cn.hutool.extra.spring.SpringUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 基本测试bean配置
 *
 * @author jay.wu
 */
@Configuration
public class BaseTestBeanConfig {

    @Bean
    public SpringUtil stringUtil() {
        return new SpringUtil();
    }
}