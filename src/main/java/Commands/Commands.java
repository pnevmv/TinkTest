package Commands;

import java.util.HashMap;

public class Commands {
    HashMap<CommandType, Command> commands;
    CommandArgsSource argSource;

    public Commands(CommandArgsSource argSource){

    }

    public enum CommandType {
        ADD,
        START,
        DELETE,
        STOP
    }
}
