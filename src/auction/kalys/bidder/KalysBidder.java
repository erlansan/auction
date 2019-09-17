package auction.kalys.bidder;


import auction.Bidder;
import auction.kalys.strategy.AggressivePurchaseStrategy;
import auction.kalys.strategy.CalmPurchaseStrategy;
import auction.kalys.strategy.PurchaseStrategy;

public class KalysBidder implements Bidder {

    private int productionQuantity;
    private int cashLimit;
    private EventSource eventSourceImpl;
    private PurchaseStrategy purchaseStrategy;
    private EventType currentState;

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
        this.currentState = EventType.TIE;
    }

    @Override
    public int placeBid() {
        if(eventSourceImpl.isEmpty()) {
            return 1;
        }
        else {
            return this.placeBidFromStory();
        }
    }

    @Override
    public void bids(int own, int other) {

        this.saveBidsInfo(own,other);

    }



    private int placeBidFromStory() {
        if(cashLimit == 0){
            return 0;
        }
        this.checkState();
        int opponentBid = this.eventSourceImpl.getAvgOpponentValue();

        int finalBid = this.purchaseStrategy.placeBid(opponentBid, cashLimit);

        if(finalBid < opponentBid){
            return Math.abs(this.purchaseStrategy.placeBid(opponentBid, cashLimit));
        }

        if(finalBid == opponentBid){
            finalBid++;
        }
        if(cashLimit < finalBid){
            return cashLimit;
        }

        return finalBid;
    }


    private void saveBidsInfo(int own, int other) {
        Event event = Event.is(own, other);
        cashLimit -= own;
        productionQuantity -= 2;
        this.eventSourceImpl.addEvent(event);
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
