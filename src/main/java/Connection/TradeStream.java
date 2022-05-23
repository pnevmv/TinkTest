package Connection;

import Data.Deal;
import Proccesor.TradeStreamProcessor;
import ru.tinkoff.piapi.contract.v1.OrderDirection;
import ru.tinkoff.piapi.contract.v1.OrderType;
import ru.tinkoff.piapi.contract.v1.Quotation;
import ru.tinkoff.piapi.core.InvestApi;
import ru.tinkoff.piapi.core.OrdersService;
import ru.tinkoff.piapi.core.stream.OrdersStreamService;

import java.util.List;
import java.util.function.Consumer;


public class TradeStream {
    private final OrdersStreamService orderStreamService;
    private final OrdersService tradeServ;
    private long orderId;
    private String accountId;

    public TradeStream(InvestApi api, String accountId) {
        orderStreamService = api.getOrdersStreamService();
        tradeServ = api.getOrdersService();
        this.accountId = accountId;
    }

    public void initialize(TradeStreamProcessor processor) {
        Consumer<Throwable> streamError = e -> System.out.println(e.toString()); //todo: correct reconnection
        orderStreamService.subscribeTrades(processor::responseProcess
                , streamError
                , List.of(accountId));
    }

    public void buyStock(long lots, Quotation price, String figi){
        tradeServ.postOrder(
                figi,
                lots,
                price,
                OrderDirection.ORDER_DIRECTION_BUY,
                accountId,
                OrderType.ORDER_TYPE_MARKET,
                String.valueOf(orderId)
        );
        orderId++;
    }
    public void sellStock(long lots, Quotation price, String figi, Deal deal){
        tradeServ.postOrder(
                figi,
                lots,
                price,
                OrderDirection.ORDER_DIRECTION_SELL,
                accountId,
                OrderType.ORDER_TYPE_MARKET,
                String.valueOf(deal.getId())
        );
    }
}
