package org.team4u.command.infrastructure.executor.liteflow;

import com.yomahub.liteflow.spring.ComponentScanner;
import com.yomahub.liteflow.util.SpringAware;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;

/**
 * LiteFlow bean配置
 *
 * @author jay.wu
 */
@Configurable
public class LiteFlowBeanConfig {

    @Bean
    public ComponentScanner componentScanner() {
        return new ComponentScanner();
    }

    @Bean
    public SpringAware springAware() {
        return new SpringAware();
    }
}