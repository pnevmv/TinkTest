package Data;

import Data.Deal;

import java.util.List;

public class OpenDeals {
    private List<Deal> openDeals;

    public OpenDeals() {
        this.openDeals = List.of();
    }

    public void addDeal(Deal deal) {
        openDeals.add(deal);
    }

    public void deleteDeal(Deal deal) {
        openDeals.remove(deal);
    }

    public void deletePartly(Deal deal, int numberOfSold) {
        addDeal(new Deal(deal.getShareNumber() - numberOfSold, deal.getPrice(), deal.getStopPrice()));
        deleteDeal(deal);
    }

    public List<Deal> getOpenDeals() {
        return this.openDeals;
    }
}
