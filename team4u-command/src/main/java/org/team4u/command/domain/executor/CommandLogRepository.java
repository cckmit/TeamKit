package org.team4u.command.domain.executor;

public interface CommandLogRepository {

    CommandLog find(String commandLogId);

    void save(CommandLog commandLog);
}