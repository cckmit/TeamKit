package org.team4u.command.infrastructure.executor.simple;


import org.team4u.base.lang.IdObject;
import org.team4u.command.domain.executor.CommandHandler;

import java.util.List;

public interface CommandRoutesBuilder extends IdObject<String> {

    List<CommandHandler> configure();
}
