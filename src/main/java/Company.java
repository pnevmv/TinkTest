import java.util.HashMap;

public class Company {
    String name;
    String figi;
    long moneyToTrade;
    long freeMoney;
    int lossPercent;
    boolean isTrading;
    int shareNum;
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

    public void tradeOn() {
        this.isTrading = true;
    }

    public void tradeOff() {
        this.isTrading = false;
    }

    public boolean getIsTrading() {
        return this.isTrading;
    }

    public void setShareNum(int number) {
        this.shareNum = number;
    }

    public int getShareNum() {
        return this.shareNum;
    }
}
