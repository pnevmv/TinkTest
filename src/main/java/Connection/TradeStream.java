package Connection;

import Data.CompanyCollection;
import Exceptions.CompanyNotFoundException;
import Exceptions.OutNumberOfReconnectAttemptsException;
import Proccesor.DataStreamProcessor;
import Proccesor.TradeStreamProcessor;
import ru.tinkoff.piapi.core.InvestApi;
import ru.tinkoff.piapi.core.stream.OrdersStreamService;


public class TradeStream {
    private CompanyCollection companies;
    private OrdersStreamService orderStreamService;

    public TradeStream(InvestApi api, CompanyCollection companies) {
        orderStreamService = api.getOrdersStreamService();
        this.companies = companies;
    }

    public void initialize(TradeStreamProcessor processor) throws OutNumberOfReconnectAttemptsException, CompanyNotFoundException {

    }
}
