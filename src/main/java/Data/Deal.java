package Data;

import java.util.Date;

public class Deal {
    private final int shareNumber;
    private final double price;
    private final Date date;
    private final double stopPrice;

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

    public Date getDate() {
        return this.date;
    }

    @Override
    public String toString() {
        return "Сделка стоимостью: " + this.getPrice()
                + "\nЦена экстренной продажи:" + this.getStopPrice()
                + "\nКол-во приобретенных акций:" + this.getShareNumber()
                + "\nДата сделки: " + this.getDate();
    }
}
