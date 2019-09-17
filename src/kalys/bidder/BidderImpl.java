package kalys.bidder;

import analyser.Analyser;
import analyser.LinearRegression;
import strategy.AggressivePurchaseStrategy;
import strategy.CalmPurchaseStrategy;
import strategy.PurchaseStrategy;

import java.util.Scanner;

public class BidderImpl implements Bidder {

    private int productionQuantity;
    private int cashLimit;
    private EventSource eventSourceImpl;
    private Analyser analyser;
    private PurchaseStrategy purchaseStrategy;
    private EventType currentState;
    private int productBid;

    @Override
    public void init(int quantity, int cash) {
        if(quantity >= 0) {
            this.productionQuantity = quantity;
        }
        else {
            throw new RuntimeException("Quantity can't be less then zero");
        }

        if(cash >= 0){
            this.cashLimit = cash;
        }
        else {
            throw new RuntimeException("Cash limit can't be less then zero");
        }
        this.eventSourceImpl = new EventSourceImpl();
        this.purchaseStrategy = new CalmPurchaseStrategy();
        this.analyser = new LinearRegression();
        this.currentState = EventType.TIE;
    }

    @Override
    public int placeBid() {
        this.productBid = (int) (Math.random() * 100);
        return productBid;
    }

    @Override
    public void bids(int own, int other) {
        if(productBid > 0) {
            int predictedBid = this.getPredictedBid(productBid);
            if(own < predictedBid){
                int offeredBid = this.predictBid(productBid);
                System.out.format("Analyser predicts, that opponents bid = %d, but your bid is less then opponent\n", predictedBid);
                System.out.format("Do you want use analyser offered bid (%d GE)? y/n\n", offeredBid);

                boolean running = true;
                while (running) {
                    Scanner scanner = new Scanner(System.in);
                    String answer = scanner.nextLine();
                    switch (answer.toUpperCase()) {
                        case "Y":
                            own = offeredBid;
                            running = false;
                            break;
                        case "N":
                            running = false;
                            break;
                        default:
                            System.out.println("The system can't understand the command (only y/n)");

                    }
                }
            }
        }
        this.saveBidsInfo(productBid,own,other);

        System.out.println("==========================");
        System.out.format("My bid: %d\n", own);
        System.out.format("Opponent bid: %d\n", other);
        System.out.println("==========================");
    }


    public int getProductionQuantity() {
        return productionQuantity;
    }


    public int getOpponentProductionQuantity() {
        return eventSourceImpl.analyseProductionQuantity(true);
    }

    /**
     * Analyse the history of bids, and gets the predict bid
     *
     * @param bid
     * Product bid
     *
     * @return Predicted bid
     */

    private int predictBid(int bid) {
        int predictedBid = this.getPredictedBid(bid);
        this.checkState();

        int finalBid = this.purchaseStrategy.placeBid(predictedBid, cashLimit);

        if(finalBid < bid){
            return Math.abs(this.purchaseStrategy.placeBid(bid, cashLimit));
        }

        if(finalBid == bid){
            finalBid++;
        }

        return finalBid;
    }


    /**
     *
     * Saves the all bids to create a history of bids. It's used to AI algorithm (Regression)
     *
     * @param product
     * Product bid
     * @param own
     * Own bid
     * @param other
     * Other bid
     */

    private void saveBidsInfo(int product, int own, int other) {
        if(own > cashLimit){
            throw new RuntimeException("It's not enough of cash limit to make some bid");
        }
        Event event = Event.is(product, own, other);
        cashLimit -= own;
        switch (event.getType()){
            case WIN:
                productionQuantity += 2;
                break;
            case TIE:
                productionQuantity++;
                break;
        }
        this.eventSourceImpl.addEvent(event);
    }

    private int getPredictedBid(int bid){
        if(!eventSourceImpl.isEmpty()) {
            this.analyser.model(eventSourceImpl.getBidForModel());
            return this.analyser.predictOpponentBids(bid);
        } else {
            return bid;
        }
    }

    private void checkState(){
        EventType state = this.getState();
        if(!currentState.equals(state)){

            if (EventType.LOSS.equals(state)) {
                this.purchaseStrategy = new AggressivePurchaseStrategy();
            } else {
                this.purchaseStrategy = new CalmPurchaseStrategy();
            }
            currentState = state;
        }
    }

    private EventType getState(){
        if(eventSourceImpl.isEmpty()) return EventType.TIE;
        return eventSourceImpl.getDominateType();
    }

}
