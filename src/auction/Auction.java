package auction;

import auction.erlan.ErlanBidder;
import auction.kalys.bidder.KalysBidder;

public class Auction {
    public static void main(String[] args) {
        Bidder kBidder = new KalysBidder();
        Bidder eBidder = new ErlanBidder();

        int quantity = 12;
        int cash = 30;

        int kalysWin = 0;
        int erlanWin = 0;

        kBidder.init(quantity, cash);
        eBidder.init(quantity, cash);

        int rounds = 1;
        while(quantity != 0)
        {
            System.out.print("Round " + rounds + "): ");
            rounds++;
            if(2<=quantity)
            {
                int kBid = kBidder.placeBid();
                int eBid = eBidder.placeBid();

                if(kBid<=0 && eBid<=0)
                {
                    System.out.println("Invalid bids: " + kBid + ", " + eBid);
                    System.out.println("Noone wins.");
                } else {
                    System.out.println("Kalys bid:" + kBid + ", Erlan bid:" + eBid);
                    if(kBid==eBid)
                    {
                        System.out.println("Same bids.");
                        kalysWin++;
                        erlanWin++;
                    }
                    if(kBid<eBid)
                    {
                        System.out.println("Erlan won.");
                        eBid += 2;
                    } else {
                        System.out.println("Kalys won.");
                        kBid += 2;
                    }
                }
                kBidder.bids(kBid, eBid);
                eBidder.bids(eBid, kBid);
                quantity -= 2;
            }
            if(1==quantity)
            {
                int kBid = kBidder.placeBid();
                int eBid = eBidder.placeBid();
                if(kBid<=0 && eBid<=0)
                {
                    System.out.println("Invalid bids: " + kBid + ", " + eBid);
                    System.out.println("Noone wins.");
                } else {
                    if(kBid==eBid)
                    {
                        System.out.println("Noone wins last 1 item.");
                    }
                    if(kBid<eBid)
                    {
                        System.out.println("Erlan won.");
                        eBid += 1;
                    } else {
                        System.out.println("Kalys won.");
                        kBid += 1;
                    }
                }

                kBidder.bids(kBid, eBid);
                eBidder.bids(eBid, kBid);
                quantity -= 1;
            }
            if(0==quantity)
            {
                System.out.println("Auction is over.");
                break;
            }
        }


    }
}
