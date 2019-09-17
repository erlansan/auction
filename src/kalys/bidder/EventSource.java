package kalys.bidder;

import analyser.BidForModel;

import java.util.List;

/**
 * Saves all events, that are happened during the auction
 */
public interface EventSource {

    /**
     * Returns the dominate type, which means, what state is now
     *
     * @return dominate type
     */
    EventType getDominateType();

    /**
     * Adds event in event source
     */
    void addEvent(Event event);

    /**
     * Checks is Event Source empty.
     */
    boolean isEmpty();

    /**
     * Converts all events in saveBidsInfo for the model
     *
     * @return structure for model
     */
    List<BidForModel> getBidForModel();

    /**
     *
     * Returns opponents or own production quantity
     *
     * @param opponent
     * is opponent quantity or not
     * @return production quantity
     */
    int analyseProductionQuantity(boolean opponent);
}
