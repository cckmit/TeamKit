package org.team4u.config.infrastructure;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.team4u.test.spring.DbTestBeanConfig;

@Configuration
@Import(DbTestBeanConfig.class)
@ComponentScan("org.team4u.config.infrastructure")
public class BeanConfig {
}