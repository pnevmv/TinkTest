package Proccesor;

import ru.tinkoff.piapi.contract.v1.Quotation;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.function.BiFunction;

public class MoneyQuotationProcessor implements Comparator<Quotation> {

    public BiFunction<Quotation, Quotation, Integer> compareByUnits = (q1, q2) ->
            Comparator.comparing(Quotation::getUnits).compare(q1, q2) ;

    public BiFunction<Quotation, Quotation, Integer> compareByNanos = (q1, q2) ->
            Comparator.comparing(Quotation::getNano).compare(q1, q2) ;

    @Override
    public int compare(Quotation o1, Quotation o2) {
        BiFunction<Quotation, Quotation, Integer> compare = (q1, q2) ->
                Comparator.comparing(Quotation::getUnits).thenComparing(Quotation::getNano).compare(q1, q2);
        return compare.apply(o1, o2);
    }

    public BigDecimal convertFromQuation(Quotation quotation ){
        return quotation.getUnits() == 0 && quotation.getNano() == 0 ? BigDecimal.ZERO : BigDecimal.valueOf(quotation.getUnits()).add(BigDecimal.valueOf(quotation.getNano(), 9));
    }

}
