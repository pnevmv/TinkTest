package Data;

import Connection.*;
import java.util.HashMap;

public class Company {
    String name;
    String figi;
    long moneyToTrade;
    long freeMoney;
    double lossPercent;
    double takeProfit;
    boolean isTrading;
    int shareNum;
    HashMap<IndexType, Index> companyIndexes;
    OpenDeals openDeals;

    public Company(String figi, long moneyToTrade, double lossPercent, double takeProfit) {
        this.figi = figi;
        this.moneyToTrade = moneyToTrade;
        this.freeMoney = moneyToTrade;
        this.lossPercent = lossPercent;
        this.takeProfit = takeProfit;
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

    public void buyShares(int lotNumber, double price) {
        double stopPrice = 0; //TODO: calculating StopPrice
        getOpenDeals().addDeal(new Deal(lotNumber, price, stopPrice));
    }

    public void sellShares (Deal deal, int lotNumber, double price) {
        getOpenDeals().deletePartly(deal, lotNumber);
        this.freeMoney += price;
    }

    public OpenDeals getOpenDeals() {
        return this.openDeals;
    }
}
