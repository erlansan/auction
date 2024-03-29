package auction.kalys.strategy;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class AggressivePurchaseStrategy extends PurchaseStrategy {

    private final double MULTIPLIER = 30.0;

    @Override
    public int placeBid(int bid, int cashLimit) {
        int multiply = super.placeBid(bid, cashLimit);
        int result = BigDecimal.valueOf(bid)
                .add(BigDecimal.valueOf((bid * MULTIPLIER * multiply)/100))
                .setScale(0, RoundingMode.HALF_UP).intValue();

        return result;
    }
}
