import Connection.CandleStream;
import ru.tinkoff.piapi.contract.v1.Candle;

public class StreamProcessor {
    CompaniesForTrading companies;
    CandleStream candleStream;



    public void process(Candle candle)  {

        try {
            //обращаемся к компании, для которой пришла свеча
            Company comp = companies.getByFigi(candle.getFigi());

            //считаем индексы для текущей свечи
           // comp.setIndexValue(IndexType.RSI,
            //        RSICalculator.calculateIndex(comp, candle));


        } catch (CompanyNotFoundException e) {
            e.printStackTrace();
        }
    }
}
