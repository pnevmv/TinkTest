package Connection;

import Data.Company;
import Data.CompanyCollection;
import Data.Deal;
import Exceptions.CompanyNotFoundException;
import Proccesor.MoneyQuotationProcessor;
import ru.tinkoff.piapi.contract.v1.OrderDirection;
import ru.tinkoff.piapi.contract.v1.OrderExecutionReportStatus;
import ru.tinkoff.piapi.contract.v1.OrderType;
import ru.tinkoff.piapi.contract.v1.Quotation;
import ru.tinkoff.piapi.core.InvestApi;
import ru.tinkoff.piapi.core.OrdersService;
import ru.tinkoff.piapi.core.stream.OrdersStreamService;


public class TradeStream {
    private final OrdersStreamService orderStreamService;
    private final OrdersService tradeServ;
    private static long orderId = 0;
    private String accountId;
    private CompanyCollection companies;

    public TradeStream(InvestApi api, String accountId, CompanyCollection companies) {
        orderStreamService = api.getOrdersStreamService();
        tradeServ = api.getOrdersService();
        this.accountId = accountId;
        this.companies = companies;
    }

   /* public void initialize(TradeStreamProcessor processor) {
        Consumer<Throwable> streamError = e -> System.out.println(e.toString());
        orderStreamService.subscribeTrades(processor::responseProcess
                , streamError
                , List.of(accountId));
    }*/

    public void buyStock(long lots, Quotation price, String figi) throws CompanyNotFoundException {
        System.out.println("going to buy");
        orderId = Double.valueOf(Math.random()).hashCode();

        var orderResponse = tradeServ.postOrderSync(
                figi,
                lots,
                price,
                OrderDirection.ORDER_DIRECTION_BUY,
                accountId,
                OrderType.ORDER_TYPE_MARKET,
                String.valueOf(orderId)
        );

        if(orderResponse.getExecutionReportStatus().equals(OrderExecutionReportStatus.EXECUTION_REPORT_STATUS_FILL) ||
                orderResponse.getExecutionReportStatus().equals(OrderExecutionReportStatus.EXECUTION_REPORT_STATUS_PARTIALLYFILL)){
            Company curComp = companies.getByFigi(figi);
            System.out.println("Куплено лотов " + orderResponse.getLotsExecuted() + " по цене " +
                    orderResponse.getExecutedOrderPrice().toString() + " \n");

            curComp.buyShares(
                    orderResponse.getLotsExecuted(),
                    MoneyQuotationProcessor.convertFromMoneyValue(orderResponse.getExecutedOrderPrice()),
                    String.valueOf(orderId));

            curComp.getOpenDeals().printDeals();
        }


    }
    public void sellStock(long lots, Quotation price, String figi, Deal deal) throws CompanyNotFoundException {
        System.out.println("going to sell");
        var orderResponse = tradeServ.postOrderSync(
                figi,
                lots,
                price,
                OrderDirection.ORDER_DIRECTION_SELL,
                accountId,
                OrderType.ORDER_TYPE_MARKET,
                String.valueOf(deal.getId() + "s")
        );

        if(orderResponse.getExecutionReportStatus().equals(OrderExecutionReportStatus.EXECUTION_REPORT_STATUS_FILL) ||
                orderResponse.getExecutionReportStatus().equals(OrderExecutionReportStatus.EXECUTION_REPORT_STATUS_PARTIALLYFILL))
        {
            Company curComp = companies.getByFigi(figi);
            System.out.println("Продано лотов " + orderResponse.getLotsExecuted() + " по цене " +
                    orderResponse.getExecutedOrderPrice().toString() + " /n");

            curComp.sellShares(
                    deal,
                    orderResponse.getLotsExecuted(),
                    MoneyQuotationProcessor.convertFromMoneyValue(orderResponse.getExecutedOrderPrice()),
                    deal.getId()
            );
            curComp.getOpenDeals().printDeals();
            System.out.println("end Of sell");
        }

    }
}
