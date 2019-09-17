package kalys.analyser;

/**
 * Data structure for model
 */

public class BidForModel {
    private final Integer bid;
    private final Integer opponentBid;

    public static BidForModel is(Integer bid, Integer opponentBid){
        return new BidForModel(bid, opponentBid);
    }

    private BidForModel(Integer bid, Integer opponentBid) {
        this.bid = bid;
        this.opponentBid = opponentBid;
    }

    public Integer getBid() {
        return bid;
    }

    public Integer getOpponentBid() {
        return opponentBid;
    }

    public Integer multiplyOfBidAndOpponentBid(){
        return bid * opponentBid;
    }
}
