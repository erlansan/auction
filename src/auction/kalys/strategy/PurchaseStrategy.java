package auction.kalys.strategy;

import java.math.BigDecimal;
import java.math.RoundingMode;

public abstract class PurchaseStrategy {

    public int placeBid(int bid, int cashLimit){
        BigDecimal difference = BigDecimal.valueOf(cashLimit).subtract(BigDecimal.valueOf(bid));

        if (difference.divide(BigDecimal.valueOf(cashLimit),2, RoundingMode.HALF_UP).doubleValue()*100 > 50){
            return 2;
        }
        return 1;
    }
}
