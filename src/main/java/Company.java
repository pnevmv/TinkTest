import ru.tinkoff.piapi.contract.v1.Candle;

import java.util.HashMap;
import java.util.List;

public class Company {
    String name;
    String figi;
    long moneyToTrade;
    long freeMoney;
    int lossPercent;
    HashMap<IndexType, Index> companyIndexes;

    public Company(String figi, long moneyToTrade, int lossPercent) {
        this.figi = figi;
        this.moneyToTrade = moneyToTrade;
        this.freeMoney = moneyToTrade;
        this.lossPercent = lossPercent;
    }

    public void startTrade(CandleSource candleSource) {
        for (Index index: companyIndexes.values()) {
            index.getHistory(figi, candleSource);
        }
    }

    public void setIndexValue(IndexType indexType, double value) {
        companyIndexes.get(indexType).setValue(value);
    }

}
