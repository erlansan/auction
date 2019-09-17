package kalys.analyser;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;

/**
 * Analyser for prediction of opponent's bid.
 * It uses linear regression function: y = a + bx
 */
public class LinearRegression implements Analyser {

    private BigDecimal intercept;
    private BigDecimal coefficient;


    @Override
    public void model(List<BidForModel> bidForModels) {

        if(this.checkDistinct(bidForModels)) {
            this.coefficient = BigDecimal.ONE;
            this.intercept = BigDecimal.ONE;
            return;
        }

        BigDecimal avgX = bidForModels.stream().map(bid -> BigDecimal.valueOf(bid.getBid())).reduce(BigDecimal::add)
                .get().divide(BigDecimal.valueOf(bidForModels.size()), 3, RoundingMode.HALF_UP);

        BigDecimal avgY = bidForModels.stream().map(bid -> BigDecimal.valueOf(bid.getOpponentBid())).reduce(BigDecimal::add)
                .get().divide(BigDecimal.valueOf(bidForModels.size()), 3, RoundingMode.HALF_UP);

        BigDecimal sumOfXY = bidForModels.stream().map(bid ->
                    {
                        BigDecimal m1 = BigDecimal.valueOf(bid.getBid()).subtract(avgX);
                        BigDecimal m2 = BigDecimal.valueOf(bid.getOpponentBid()).subtract(avgY);
                        return m1.multiply(m2);
                    }
                )
                .reduce(BigDecimal::add).get();

        BigDecimal sumOfXPower = bidForModels.stream().map(bid -> {
            BigDecimal x = BigDecimal.valueOf(bid.getBid()).subtract(avgX);
            return x.pow(2);
        }).reduce(BigDecimal::add).get();

        BigDecimal sumOfYPower = bidForModels.stream().map(bid -> {
            BigDecimal y = BigDecimal.valueOf(bid.getOpponentBid()).subtract(avgY);
            return y.pow(2);
        }).reduce(BigDecimal::add).get();

        BigDecimal R = this.countR(sumOfXY, sumOfXPower, sumOfYPower);
        BigDecimal SY = this.countSY(sumOfYPower, bidForModels.size());
        BigDecimal SX = this.countSX(sumOfXPower, bidForModels.size());

        this.coefficient = this.countCoefficient(R, SX, SY);
        this.intercept = this.countIntercept(avgX, avgY, coefficient);
    }

    @Override
    public int predictOpponentBids(int bid) {
        this.check();
        return this.predict(bid);
    }

    private BigDecimal countIntercept(BigDecimal avgX, BigDecimal avgY, BigDecimal coefficient){
        return avgY.subtract(avgX.multiply(coefficient));
    }

    private BigDecimal countCoefficient(BigDecimal R, BigDecimal SX, BigDecimal SY){
        return R.multiply(SY.divide(SX, 3, RoundingMode.HALF_UP));
    }


    private BigDecimal countR(BigDecimal sumOfXY, BigDecimal sumOfXPower, BigDecimal sumOfYPorwer){
        return sumOfXY.divide(sumOfXPower.multiply(sumOfYPorwer).sqrt(MathContext.DECIMAL64), 8, RoundingMode.HALF_UP);
    }


    private BigDecimal countSY(BigDecimal sumOfYPower, int n){
        return sumOfYPower.divide(BigDecimal.valueOf(n-1), 8, RoundingMode.HALF_UP).sqrt(MathContext.DECIMAL64);
    }

    private BigDecimal countSX(BigDecimal sumOfXPower, int n){
        return sumOfXPower.divide(BigDecimal.valueOf(n-1), 8, RoundingMode.HALF_UP).sqrt(MathContext.DECIMAL64);
    }

    /**
     * Function of linear regression
     * y = a + bx
     * a - intercept
     * b - coefficient
     */
    private int predict(int x){
        return intercept
                .add(BigDecimal.valueOf(x)
                        .multiply(coefficient))
                .setScale(0, RoundingMode.HALF_UP)
                .intValue();
    }

    private void check(){
        if (intercept == null || coefficient == null) throw new RuntimeException("There are no coefficient or intercept");
    }

    /**
     * It's needed to avoid Null Pointer Exception. TODO: Find another solution
     * */
    private boolean checkDistinct(List<BidForModel> bidForModels){
        long sizeOfBids = bidForModels.stream().map(BidForModel::getBid).distinct().count();
        long sizeOfOpponentBids = bidForModels.stream().map(BidForModel::getOpponentBid).distinct().count();
        return sizeOfBids == 1 || sizeOfOpponentBids == 1;
    }

    public BigDecimal getIntercept() {
        return intercept;
    }

    public BigDecimal getCoefficient() {
        return coefficient;
    }
}
