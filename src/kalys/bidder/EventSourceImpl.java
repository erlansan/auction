package kalys.bidder;

import kalys.analyser.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EventSourceImpl implements EventSource{
    private List<Event> events;

    public EventSourceImpl() {
        events = new ArrayList<>();
    }

    @Override
    public EventType getDominateType(){
        Map<EventType, Long> map = events.stream().collect(Collectors.groupingBy(Event::getType, Collectors.counting()));

        return map.entrySet()
                .stream()
                .sorted(Comparator.comparing(Map.Entry::getKey, Enum::compareTo))
                .max(Comparator.comparing(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .orElseThrow(() -> new RuntimeException("There is no event type"));
    }

    @Override
    public void addEvent(Event event) {
        this.events.add(event);
    }

    @Override
    public boolean isEmpty(){
        return this.events.isEmpty() || this.events.size() == 1;
    }

    @Override
    public List<BidForModel> getBidForModel(){
        return events.stream().map(event -> BidForModel.is(event.getProductBid(), event.getOpponentBid())).collect(Collectors.toList());
    }

    @Override
    public int analyseProductionQuantity(boolean opponent){
        return this.events.stream().mapToInt(event -> {
            if(opponent){
                if (EventType.LOSS.equals(event.getType())){
                    return 2;
                }
                if (EventType.TIE.equals(event.getType())){
                    return 1;
                }
            }
            else {
                if (EventType.WIN.equals(event.getType())){
                    return 2;
                }
                if (EventType.TIE.equals(event.getType())){
                    return 1;
                }
            }
            return 0;
        }).reduce(0, Integer::sum);
    }
}
