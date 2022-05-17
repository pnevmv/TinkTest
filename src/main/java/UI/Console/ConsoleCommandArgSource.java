package UI.Console;

import Commands.CommandArgsSource;
import Commands.Commands;
import Exceptions.IllegalCommandArgsException;
import Exceptions.NoSuchCommandException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.function.Supplier;

public class ConsoleCommandArgSource implements CommandArgsSource {
    Scanner in;
    HashMap<Commands.CommandType , Supplier<List<String>>> commandArgsMap;

    public ConsoleCommandArgSource(Scanner in){
        this.in = in;
        commandArgsMap.put(Commands.CommandType.ADD, this::AddArgs);

    }

    @Override
    public Supplier<List<String>> getArgsByCommand(Commands.CommandType commandType) throws  NoSuchCommandException {
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
        System.out.println("Input percent of takeprofit - starting from wich percent bot can sell share (integer)");
        res.add(in.nextLine().trim());

        return res;
    }
}
