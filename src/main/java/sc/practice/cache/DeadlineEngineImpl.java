package sc.practice.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

public class DeadlineEngineImpl implements DeadlineEngine {

    private final AtomicLong requestId ;
    private final Map<Long, Long> deadLineStore;

    public DeadlineEngineImpl(){
        requestId = new AtomicLong();
        deadLineStore = new ConcurrentHashMap<>();
    }


    @Override
    public long schedule(long deadlineMs){
        long val = requestId.incrementAndGet();
        deadLineStore.put(val, deadlineMs);
        return val;
    }

    @Override
    public boolean cancel(long requestId) {
        return deadLineStore.remove(requestId) != null;
    }

     @Override
     public int poll(long nowMs, Consumer<Long> handler, int maxPoll){
        int count = 0;
        for (Map.Entry<Long, Long> e : deadLineStore.entrySet()) {
            if(e.getValue() <= nowMs){
                handler.accept(e.getKey());
                count++;
            }
            if(count == maxPoll) break;
        }

        return count;
     }
     

     @Override
     public int size(){
         return deadLineStore.size();
     }
}