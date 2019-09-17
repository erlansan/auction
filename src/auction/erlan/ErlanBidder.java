package auction.erlan;

import auction.Bidder;

public class ErlanBidder implements Bidder {

    private int quantity = 0;
    private int myQuantity = 0;

    private int cash = 0;

    private int totalRounds = 0;
    private int currentRound = 1;

    public ErlanBidder() {
        //nothing happens;
    }

    @Override
    public void init(int quantity, int cash) {
        this.quantity = quantity;
        this.cash = cash;

        if(quantity%2==0)
        {
            this.totalRounds = quantity/2;
        } else {
            this.totalRounds = quantity/2 + 1;
        }
    }

    @Override
    public int placeBid() {
        if(0==cash || currentRound==totalRounds)
        {
            return 0;
        }

        if(1==currentRound)
        {
            currentRound++;
            return 0;
        }

        int bid = cash/(totalRounds - currentRound) + 1;
        currentRound++;
        if(bid<cash)
        {
            cash -= bid;
            return bid;
        } else {
            bid = cash;
            cash -= bid;
            return bid;
        }
    }

    @Override
    public void bids(int own, int other) {

    }
}
