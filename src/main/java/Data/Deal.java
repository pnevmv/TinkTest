package Data;

import java.util.Date;

public class Deal {
    private int shareNumber;
    private double price;
    private Date date;
    private double stopPrice;

    public Deal(int shareNumber, double price, double stopPrice) {
        this.shareNumber = shareNumber;
        this.price = price;
        this.date = new Date(System.currentTimeMillis());
        this.stopPrice = stopPrice;
    }

    public int getShareNumber() {
        return this.shareNumber;
    }

    public double getPrice() {
        return this.price;
    }

    public double getStopPrice() {
        return this.stopPrice;
    }
}
