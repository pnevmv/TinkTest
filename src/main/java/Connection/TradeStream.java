package Connection;

import Data.CompanyCollection;
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
    private final InvestApi api;
    private final CompanyCollection companies;
    private final OrdersStreamService orderStreamService;
    private final OrdersService tradeServ;
    private long orderId;

    public TradeStream(InvestApi api, CompanyCollection companies) {
        this.api = api;
        orderStreamService = api.getOrdersStreamService();
        tradeServ = api.getOrdersService();
        this.companies = companies;
    }

    public void initialize(TradeStreamProcessor processor) {
        Consumer<Throwable> streamError = e -> System.out.println(e.toString()); //todo: logger, correct reconnection
        orderStreamService.subscribeTrades(processor::responseProcess
                , streamError
                , List.of(api.getUserService().getAccountsSync().get(0).getId()));
        //todo: сделать нормальное получение акаауннтов
    }

    public void buyStock(long lots, Quotation price, String figi){
        tradeServ.postOrder(
                figi,
                lots,
                price,
                OrderDirection.ORDER_DIRECTION_BUY,
                api.getUserService().getAccountsSync().get(0).getId(),
                OrderType.ORDER_TYPE_MARKET,
                String.valueOf(orderId)
        ); //todo: сделать нормальное получение акаауннтов
        orderId++;
    }
    public void sellStock(long lots, Quotation price, String figi, Deal deal){
        tradeServ.postOrder(
                figi,
                lots,
                price,
                OrderDirection.ORDER_DIRECTION_SELL,
                api.getUserService().getAccountsSync().get(0).getId(),
                OrderType.ORDER_TYPE_MARKET,
                String.valueOf(deal.getId())
        ); //todo: сделать нормальное получение акаауннтов
    }
}
