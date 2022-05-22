package UI.Console;

import Commands.CommandArgsSource;
import Commands.CommandType;
import Exceptions.NoSuchCommandException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.function.Supplier;

public class ConsoleCommandArgSource implements CommandArgsSource {
    Scanner in;
    HashMap<CommandType, Supplier<List<String>>> commandArgsMap;

    {
        commandArgsMap = new HashMap<>();
    }

    public ConsoleCommandArgSource(Scanner in){
        this.in = in;
        commandArgsMap.put(CommandType.ADD, this::AddArgs);
        commandArgsMap.put(CommandType.DELETE, this::DeleteArgs);
        commandArgsMap.put(CommandType.START, this::StartArgs);
        commandArgsMap.put(CommandType.STOP, this::StopArgs);
    }

    @Override
    public Supplier<List<String>> getArgsByCommand(CommandType commandType) throws  NoSuchCommandException {
        if(!commandArgsMap.containsKey(commandType)) throw new NoSuchCommandException();
        return commandArgsMap.get(commandType);
    }

    private List<String> AddArgs(){
        List<String> res = new ArrayList<>();

        System.out.println("Input figi of company");
        res.add(in.nextLine().trim());
        System.out.println("Input money in rubles to trade shares of this company (integer)");
        res.add(in.nextLine().trim());
        System.out.println("Input percent of loss you can loose in one deal (integer)");
        res.add(in.nextLine().trim());
        System.out.println("Input percent of take-profit - starting from which percent bot can sell share (integer)");
        res.add(in.nextLine().trim());

        return res;
    }

    private List<String> DeleteArgs(){
        List<String> res = new ArrayList<>();

        System.out.println("Input figi of company you want to delete. All trading with this company will stop");
        res.add(in.nextLine().trim());

        return res;
    }

    private List<String> StartArgs(){
        List<String> res = new ArrayList<>();

        System.out.println("Input figi of company you want starts to trade");
        res.add(in.nextLine().trim());

        return res;
    }

    private List<String> StopArgs(){
        List<String> res = new ArrayList<>();

        System.out.println("Input figi of company you want bot starts to trade");
        res.add(in.nextLine().trim());

        return res;
    }


}
