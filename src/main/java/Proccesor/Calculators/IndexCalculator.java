package Proccesor.Calculators;

import Data.Company;
import ru.tinkoff.piapi.contract.v1.Candle;

/**
 * Behavior of classes that calculate indexes
 */
public interface IndexCalculator {
    double calculateIndex(Company company, Candle candle);
}