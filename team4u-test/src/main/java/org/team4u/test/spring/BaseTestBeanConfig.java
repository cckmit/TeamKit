package org.team4u.test.spring;

import cn.hutool.extra.spring.SpringUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.team4u.base.spring.SpringInitializedPublisher;

/**
 * 基本测试bean配置
 *
 * @author jay.wu
 */
@Import({SpringUtil.class, SpringInitializedPublisher.class})
@Configuration
public class BaseTestBeanConfig {
}