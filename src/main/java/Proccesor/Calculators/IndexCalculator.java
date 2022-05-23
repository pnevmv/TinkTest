package Proccesor.Calculators;

import Data.Company;
import ru.tinkoff.piapi.contract.v1.Candle;

/**
 * Behavior of classes that calculate indexes.
 */
public interface IndexCalculator {
    /**
     *
     * @param company - company you calculate index for
     * @param candle - candle wuth new information about company
     * @return
     */
    double calculateIndex(Company company, Candle candle);
}