package Data;

import Connection.*;
import java.util.HashMap;

public class Company {
    private String name; //TODO: finding names by figi
    private final String figi;
    private double moneyToTrade;
    private double freeMoney;
    private double lossPercent;
    private double takeProfit;
    private boolean isTrading;
    private int shareNumber;
    private int lot;
    private HashMap<IndexType, Index> companyIndexes;
    private OpenDeals openDeals;

    public Company(String figi, long moneyToTrade, double lossPercent, double takeProfit, int lot,  HashMap<IndexType, Index> companyIndexes) {
        this.figi = figi;
        this.moneyToTrade = moneyToTrade;
        this.freeMoney = moneyToTrade;
        this.lossPercent = lossPercent;
        this.takeProfit = takeProfit;
        this.lot = lot;
        this.companyIndexes = companyIndexes;
        this.shareNumber = 0;
        this.isTrading = false;
    }

    public Company(String figi, double moneyToTrade, double freeMoney, double lossPercent, double takeProfit, boolean isTrading, int shareNumber, int lot, HashMap<IndexType, Index> companyIndexes, OpenDeals openDeals) {
        this.figi = figi;
        this.moneyToTrade = moneyToTrade;
        this.freeMoney = freeMoney;
        this.lossPercent = lossPercent;
        this.takeProfit = takeProfit;
        this.isTrading = isTrading;
        this.shareNumber = shareNumber;
        this.lot = lot;
        this.companyIndexes = companyIndexes;
        this.openDeals = openDeals;
    }

    public void startTrade(CandleSource candleSource) {
        for (Index index: companyIndexes.values()) {
            index.loadHistory(figi, candleSource); //TODO:calculating initial indexes
        }
        this.isTrading = true;
    }

    public String getFigi() {
        return this.figi;
    }

    public void setLossPercent(double value) {
        this.lossPercent = value;
    }

    public void setTakeProfit(double value) {
        this.takeProfit = value;
    }

    public double getMoneyToTrade() {
        return this.moneyToTrade;
    }

    public double getFreeMoney() {
        return this.freeMoney;
    }

    public void setIndexValue(IndexType indexType, double value) {
        companyIndexes.get(indexType).setValue(value);
    }

    public Index getIndexByType(IndexType indexType) {
        return this.companyIndexes.get(indexType);
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
        this.shareNumber = number;
    }

    public int getShareNumber() {
        return this.shareNumber;
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

    public String getName() {
        return this.name;
    }

    public double getLossPercent() {
        return this.lossPercent;
    }

    public double getTakeProfit() {
        return this.takeProfit;
    }

    @Override
    public String toString() {
        return "Компания: " + this.getName()
                + "\nfigi: " + this.getFigi()
                + "\nКол-во доступных средств на покупку: " + this.getFreeMoney()
                + "\nДопустимый процент потерь: " + this.getLossPercent()
                + "\nЦель-профит: " + this.getTakeProfit()
                + "\nТрейдинг-статус: " + this.getIsTrading()
                + "\nКол-во купленных акций: " + this.getShareNumber();
    }
}
