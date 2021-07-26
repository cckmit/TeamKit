package org.team4u.test.spring;

import cn.hutool.extra.spring.SpringUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 基本测试bean配置
 *
 * @author jay.wu
 */
@Import(SpringUtil.class)
@Configuration
public class BaseTestBeanConfig {
}