package Proccesor;

import Data.Company;
import Data.CompanyCollection;
import Data.Deal;
import Exceptions.CompanyNotFoundException;
import ru.tinkoff.piapi.contract.v1.OrderTrade;
import ru.tinkoff.piapi.contract.v1.TradesStreamResponse;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

public class TradeStreamProcessor {
    CompanyCollection companies;

    public TradeStreamProcessor(CompanyCollection companies) {
        this.companies = companies;
    }


    /*
  todo: обработать ситуации, когда есть кос€ки с инфой с бирже и историей сделок. ƒобавить в качестве пол€
    вывод в консоль(или лучше экземпл€р юзер интерфейса) и коннектор, чтобы удал€ть и стопить подписки если с ними че то стало не так
   */

    public void responseProcess(TradesStreamResponse tradesStreamResponse) {
        if(tradesStreamResponse.hasOrderTrades()){
            try {
                Company curComp = companies.getByFigi(tradesStreamResponse.getOrderTrades().getFigi());
                String dealId =  tradesStreamResponse.getOrderTrades().getOrderId();

                switch(tradesStreamResponse.getOrderTrades().getDirection()){

                    case ORDER_DIRECTION_BUY:
                        BigDecimal tradesSum = BigDecimal.ZERO;
                        long lotsSum = 0;
                        //считаетс€ средн€€ цена прошедших сделок, и записываетс€ в openDeals (через метод buyShares)
                        for(OrderTrade trade : tradesStreamResponse.getOrderTrades().getTradesList()){
                            //суммарное количество лотов
                            lotsSum += trade.getQuantity();
                            //суммарна€ стоимость
                            tradesSum = tradesSum.add(
                                    MoneyQuotationProcessor.convertFromQuation(trade.getPrice()).
                                    multiply(BigDecimal.valueOf(trade.getQuantity()))
                            );
                        }
                        curComp.buyShares(
                                lotsSum,
                                tradesSum.divide(BigDecimal.valueOf(lotsSum), 9, RoundingMode.HALF_DOWN),
                                dealId);
                        break;

                    case ORDER_DIRECTION_SELL:
                        //todo: exception id ответа не соответствует ни одному хрнаимому дилу, мб попробовать искать по времени
                        //ищем сделку в списке сделок по айди, если не находим вылетает исключение
                        Optional<Deal> curDeal = curComp.getOpenDeals().getDealById(dealId);
                        //todo: можно переписать через orElseThrow
                        if(curDeal.isEmpty()) break;

                        for(OrderTrade trade : tradesStreamResponse.getOrderTrades().getTradesList()){
                            curComp.sellShares(
                                    curDeal.get(),
                                    trade.getQuantity(),
                                    MoneyQuotationProcessor.convertFromQuation(trade.getPrice()),
                                    tradesStreamResponse.getOrderTrades().getOrderId());
                        }
                        break;

                    default:
                        break;
                }
            } catch (CompanyNotFoundException e) {
                e.printStackTrace();
            }

        }


    }

}
