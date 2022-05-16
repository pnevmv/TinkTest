package Proccesor;

import Connection.CandleStream;
import Data.CompaniesForTrading;
import Data.Company;
import Data.CompanyNotFoundException;
import ru.tinkoff.piapi.contract.v1.Candle;

public class StreamProcessor {
    CompaniesForTrading companies;
    CandleStream candleStream;



    public void process(Candle candle)  {

        try {
            //���������� � ��������, ��� ������� ������ �����
            Company comp = companies.getByFigi(candle.getFigi());

            //������� ������� ��� ������� �����
           // comp.setIndexValue(Data.IndexType.RSI,
            //        Proccesor.RSICalculator.calculateIndex(comp, candle));


        } catch (CompanyNotFoundException e) {
            e.printStackTrace();
        }
    }
}
