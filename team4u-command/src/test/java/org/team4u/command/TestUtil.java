package org.team4u.command;

import org.team4u.command.domain.config.CommandConfig;
import org.team4u.command.infrastructure.config.CommandConfigService;
import org.team4u.command.infrastructure.config.JsonCommandConfigRepository;

/**
 * @author jay.wu
 */
public class TestUtil {

    private static final JsonCommandConfigRepository jsonCommandConfigRepository
            = new JsonCommandConfigRepository(new CommandConfigService());

    public static CommandConfig configOf(String path) {
        return jsonCommandConfigRepository.configOfId(path);
    }
}