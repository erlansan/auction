package auction;

import auction.erlan.ErlanBidder;
import auction.kalys.bidder.KalysBidder;

public class Auction {
    public static void main(String[] args) {
        Bidder kBidder = new KalysBidder();
        Bidder eBidder = new ErlanBidder();

        int quantity = 30;
        int cash = 30;

        kBidder.init(quantity, cash);
        eBidder.init(quantity, cash);

        while(quantity != 0)
        {
            if(2<=quantity)
            {
                //TODO aw;ldkawl;dkaw;ldaw;

                quantity -= 2;
            }
            if(1==quantity)
            {
                //TODO

                quantity -= 1;
            }
            if(0==quantity)
            {
                break;
            }
        }


    }
}
