package kalys.bidder;

/**
 *  bidder.Event represents what happened after receiving of saveBidsInfo
 */

public class Event {

    private final EventType type;
    private final int productBid;
    private final int ownBid;
    private final int opponentBid;

    public static Event is(int productBid, int ownBid, int opponentBid){
        EventType type;

        if(ownBid > opponentBid){
            type = EventType.WIN;
        } else if(opponentBid > ownBid){
            type = EventType.LOSS;
        } else {
            type = EventType.TIE;
        }

        return new Event(type, productBid, ownBid, opponentBid);
    }


    private Event(EventType type, int productBid, int ownBid, int opponentBid) {
        this.validate(type, productBid, ownBid, opponentBid);
        this.type = type;
        this.productBid = productBid;
        this.ownBid = ownBid;
        this.opponentBid = opponentBid;
    }

    /**Validation*/
    private void validate(EventType type, int productBid, int ownBid, int opponentBid){

        if((productBid > ownBid) || (productBid > opponentBid)){
            throw new RuntimeException("Product bid can't be bigger own bid or opponent's bid");
        }

        // if type is WIN, but opponent bid is bigger than own, throw exception
        if(EventType.WIN.equals(type) && (opponentBid > ownBid)){
            throw new RuntimeException("It can be added WIN event, where opponent's bid is bigger than own");
        }

        // if type is WIN, but opponent bid is bigger than own, throw exception
        if(EventType.LOSS.equals(type) && (ownBid > opponentBid)){
            throw new RuntimeException("It can be added LOSS event, where own bid is bigger than opponent's");
        }

        // if type is WIN, but opponent bid is bigger than own, throw exception
        if(EventType.TIE.equals(type) && (opponentBid != ownBid)){
            throw new RuntimeException("It can be added TIE event, where opponent bid is not equal to own");
        }
    }

    /**=====================GETTERS AND SETTERS===========================*/
    public EventType getType() {
        return type;
    }

    public int getProductBid() {
        return productBid;
    }


    public int getOwnBid() {
        return ownBid;
    }

    public int getOpponentBid() {
        return opponentBid;
    }

    /**===================================================================*/
}
