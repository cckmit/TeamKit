package org.team4u.command.domain.config;

public interface CommandConfigRepository {

    CommandConfig configOf(String id);

    void save(CommandConfig config);
}