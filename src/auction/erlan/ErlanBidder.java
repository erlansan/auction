package auction.erlan;

import auction.Bidder;

public class ErlanBidder implements Bidder {

    private int quantity = 0;
    private int cash = 0;

    public ErlanBidder() {
        //nothing happens;
    }

    @Override
    public void init(int quantity, int cash) {
        this.quantity = quantity;
        this.cash = cash;
    }

    @Override
    public int placeBid() {

        return 0;
    }

    @Override
    public void bids(int own, int other) {

    }
}
