package Connection;

import Data.CompanyCollection;
import Exceptions.CompanyNotFoundException;
import Exceptions.OutNumberOfReconnectAttemptsException;
import Proccesor.TradeStreamProcessor;
import ru.tinkoff.piapi.contract.v1.OrderDirection;
import ru.tinkoff.piapi.contract.v1.OrderType;
import ru.tinkoff.piapi.contract.v1.Quotation;
import ru.tinkoff.piapi.contract.v1.TradesStreamResponse;
import ru.tinkoff.piapi.core.InvestApi;
import ru.tinkoff.piapi.core.OrdersService;
import ru.tinkoff.piapi.core.stream.OrdersStreamService;

import java.util.List;
import java.util.function.Consumer;


public class TradeStream {
    private InvestApi api;
    private CompanyCollection companies;
    private OrdersStreamService orderStreamService;
    private OrdersService tradeServ;
    private long orderId;

    {
        orderId = 0;
    }
    public TradeStream(InvestApi api, CompanyCollection companies) {
        this.api = api;
        orderStreamService = api.getOrdersStreamService();
        tradeServ = api.getOrdersService();
        this.companies = companies;
    }

    public void initialize(TradeStreamProcessor processor) throws OutNumberOfReconnectAttemptsException, CompanyNotFoundException {
        Consumer<Throwable> streamError = e -> {System.out.println(e.toString()); }; //todo: logger, correct reconnection
        orderStreamService.subscribeTrades(this::responseProcess, streamError, List.of(api.getUserService().getAccountsSync().get(0).getId()));
        //todo: сделать нормальное получение акаауннтов
    }

    public boolean buyStock(int lots, Quotation price, String figi){
        tradeServ.postOrderSync(
                figi,
                lots,
                price,
                OrderDirection.ORDER_DIRECTION_BUY,
                api.getUserService().getAccountsSync().get(0).getId(),
                OrderType.ORDER_TYPE_MARKET,
                String.valueOf(orderId)
        ); //todo: сделать нормальное получение акаауннтов
        orderId++;
        return true;
    }
    public boolean sellStock(int lots, Quotation price, String figi){
        tradeServ.postOrderSync(
                figi,
                lots,
                price,
                OrderDirection.ORDER_DIRECTION_SELL,
                api.getUserService().getAccountsSync().get(0).getId(),
                OrderType.ORDER_TYPE_MARKET,
                String.valueOf(orderId)
        ); //todo: сделать нормальное получение акаауннтов
        orderId++;
        return false;
    }

    private void responseProcess(TradesStreamResponse tradesStreamResponse) {
        if(tradesStreamResponse.hasOrderTrades()){
            tradesStreamResponse.getOrderTrades();

        }
    }
}
