package Proccesor;

import Data.Company;
import Proccesor.IndexCalculator;
import ru.tinkoff.piapi.contract.v1.Candle;

public class RSICalculator implements IndexCalculator {

    @Override
    public double calculateIndex(Company company, Candle candle) {
        return 0;
    }
}
