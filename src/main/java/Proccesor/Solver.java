package Proccesor;

import Data.Company;
import Data.IndexType;

public class Solver {

    /**
     * Class that calculates probability to buy/sell stocks from indexes.
     * Solution to buy/sell represented as double in range [-1; 1] where '-' is to sell,
     * '+' is to buy and value is probability to do this
     * In future you can use ml to find optional coefficients for each index, or another model to combine them
     *
     * @param company
     * @return Solution to buy/sell as double in range [-1; 1] where '-' is to sell,
     * '+' is to buy and value is probability to do this thing
     */
    public static double  solution(Company company){
        return RSIOnlyVersion(company); //todo: algorithm for combining indexes. Now algorithm is only for RSI
    }

    private static double RSIOnlyVersion(Company company){
        double high = 70;
        double low = 30;
        double rsi = company.getIndexByType(IndexType.RSI).getValue();
        if(rsi > high){
            return (rsi - high) / (100 - high); // if rsi = 100 probability 100%, if 70 probability 0%
        }
        if(rsi < low){
            return (rsi - low) / (low); // if rsi = 0 probability -100% (negative is signal to sell), if 30 probability 0%
        }
        return 0;
    }
}
