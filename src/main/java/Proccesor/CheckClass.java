package Proccesor;

import ru.tinkoff.piapi.contract.v1.Candle;
import ru.tinkoff.piapi.contract.v1.HistoricCandle;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

public class CheckClass {
    private ArrayDeque<Candle> candleHistory;

    public CheckClass(){
        candleHistory = new ArrayDeque<Candle>();

    }
    public void updateHistory(Candle newCandle){
        candleHistory.addLast(newCandle);
    }
    public void printquue(){
        for(Candle c : candleHistory){
            System.out.println(c);
        }
    }

    public long getSecsOfLAstCAndle(){
        return candleHistory.getLast().getTime().getSeconds();
    }
}
