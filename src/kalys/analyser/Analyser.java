package kalys.analyser;

import java.util.List;

/**
 * Analyser for prediction of opponent's bid.
 */
public interface Analyser {

    /**
     * Creates model
     *
     * @param bidForModels
     * data set
     */
    void model(List<BidForModel> bidForModels);

    /**
     * Predicts opponent bid
     *
     * @param bid
     * product bid
     *
     * @return predicted opponent bid
     */
    int predictOpponentBids(int bid);
}
