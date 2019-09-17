package auction.kalys.bidder;

/**
 *  bidder.Event represents what happened after receiving of saveBidsInfo
 */

public class Event {

    private final EventType type;
    private final int ownBid;
    private final int opponentBid;

    public static Event is(int ownBid, int opponentBid){
        EventType type;

        if(ownBid > opponentBid){
            type = EventType.WIN;
        } else if(opponentBid > ownBid){
            type = EventType.LOSS;
        } else {
            type = EventType.TIE;
        }

        return new Event(type, ownBid, opponentBid);
    }


    private Event(EventType type, int ownBid, int opponentBid) {
        this.type = type;
        this.ownBid = ownBid;
        this.opponentBid = opponentBid;
    }


    /**=====================GETTERS AND SETTERS===========================*/
    public EventType getType() {
        return type;
    }

    public int getOwnBid() {
        return ownBid;
    }

    public int getOpponentBid() {
        return opponentBid;
    }

    /**===================================================================*/
}
