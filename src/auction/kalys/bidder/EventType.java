package auction.kalys.bidder;

public enum EventType {
    WIN(1),
    LOSS(2),
    TIE(3);

    private int privilege;

    EventType(int privilege) {
        this.privilege = privilege;
    }

    public int getPrivilege() {
        return privilege;
    }
}
