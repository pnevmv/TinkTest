package Proccesor;

import Data.IndexType;
import Proccesor.Calculators.IndexCalculator;
import Proccesor.Calculators.NVICalculator;
import Proccesor.Calculators.PVICalculator;
import Proccesor.Calculators.RSICalculator;

import java.util.HashMap;

public class IndexCalculators {
    static HashMap<IndexType, IndexCalculator> calcMaps;

    static {
        calcMaps.put(IndexType.RSI, new RSICalculator());
        calcMaps.put(IndexType.NVI, new NVICalculator());
        calcMaps.put(IndexType.PVI, new PVICalculator());
    }

    public static IndexCalculator getCalcByIndex(IndexType t){ return calcMaps.get(t);}
    public static void addCalculator(IndexType t, IndexCalculator c){
        calcMaps.put(t, c);
    }
}
