package Proccesor;

import ru.tinkoff.piapi.contract.v1.Candle;

import java.util.ArrayDeque;

/**
 * just class for testing indexes calculation. Nevermind
 */
public class CheckClass {
    private ArrayDeque<Candle> candleHistory;

    public CheckClass(){
        candleHistory = new ArrayDeque<Candle>();

    }
    public void updateHistory(Candle newCandle){
        candleHistory.addLast(newCandle);
    }
    public void printQueue(){
        for(Candle c : candleHistory){
            System.out.println(c);
        }
    }

    public long getSecsOfLAstCandle(){
        return candleHistory.getLast().getTime().getSeconds();
    }
}
