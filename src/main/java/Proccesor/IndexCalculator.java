package Proccesor;

import Data.Company;
import ru.tinkoff.piapi.contract.v1.Candle;

public interface IndexCalculator {
    double calculateIndex(Company company, Candle candle);
}