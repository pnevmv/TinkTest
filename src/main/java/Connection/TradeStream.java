package Connection;

import Data.CompanyCollection;
import Data.CompanyNotFoundException;
import Proccesor.StreamProcessor;
import ru.tinkoff.piapi.core.InvestApi;
import ru.tinkoff.piapi.core.stream.OrdersStreamService;


public class TradeStream {
    private CompanyCollection companies;
    private OrdersStreamService orderStreamService;

    public TradeStream(InvestApi api, CompanyCollection companies) {
        orderStreamService = api.getOrdersStreamService();
        this.companies = companies;
    }

    public void initialize(StreamProcessor processor) throws OutNumberOfReconnectAttemptsException, CompanyNotFoundException {

    }
}
