package Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class OpenDeals {
    private List<Deal> openDeals;

    public OpenDeals() {
        this.openDeals = new ArrayList<>();
    }

    public void addDeal(Deal deal) {
        openDeals.add(deal);
    }

    private void deleteDeal(Deal deal) {
        openDeals.remove(deal);
    }

    public void deletePartly(Deal deal, long numberOfSoldLots) {
        if(deal.getLotNumber() - numberOfSoldLots != 0) {
            addDeal(new Deal(deal.getLotNumber() - numberOfSoldLots,
                    deal.getPrice(),
                    deal.getStopPrice(),
                    deal.getId()));
        }
        deleteDeal(deal);
    }

    public List<Deal> getDealsAsList() {
        return this.openDeals;
    }

    public void sortByPrices() {
        this.openDeals.sort(Comparator.comparing(Deal::getPrice));
    }

    public BigDecimal getAveragePrice() {
        BigDecimal price = BigDecimal.ZERO;
        for (Deal deal: openDeals) {
            price = price.add(deal.getPrice());
        }

        return price.divide(BigDecimal.valueOf(openDeals.size()), 9, RoundingMode.HALF_DOWN);
    }

    @Override
    public String toString() {
        return "Кол-во сделок: " + this.openDeals.size()
                + "Средняя стоимость всех акций: " + getAveragePrice();
    }

    public Optional<Deal> getDealById(String id){
        for(Deal d : openDeals){
            if(id.equals(d.getId())){
                return Optional.of(d);
            }
        }
        return Optional.empty();
    }
}
