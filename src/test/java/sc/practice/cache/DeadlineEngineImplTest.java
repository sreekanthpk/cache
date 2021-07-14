package sc.practice.cache;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.function.Consumer;

@RunWith(MockitoJUnitRunner.class)
public class DeadlineEngineImplTest {

    @Mock
    Consumer<Long> consumer;

    @Captor
    private ArgumentCaptor<Long> consumerArgCaptor;

    @Test
    public void testSchedule(){
        DeadlineEngine deadlineEngine = new DeadlineEngineImpl();
        long requestId = deadlineEngine.schedule(100);
        Assert.assertEquals(1, requestId);
    }

    @Test
    public void testPoll(){
        DeadlineEngine deadlineEngine = new DeadlineEngineImpl();
        long requestId = deadlineEngine.schedule(100);
        Consumer<Long> consumer = (Long x) -> System.out.println(x);
        int count = deadlineEngine.poll(1000,  consumer , 1);
        Assert.assertEquals(1, count);
    }

    @Test
    public void testPollMaxPoll(){
        DeadlineEngine deadlineEngine = new DeadlineEngineImpl();
        long requestId1 = deadlineEngine.schedule(100);
        long requestId2 = deadlineEngine.schedule(200);
        Consumer<Long> consumer = (Long x) -> System.out.println(x);
        int count = deadlineEngine.poll(1000,  consumer , 1);
        Assert.assertEquals(1, count);
    }

    @Test
    public void testPollMultipleNotifications(){
        DeadlineEngine deadlineEngine = new DeadlineEngineImpl();
        long requestId1 = deadlineEngine.schedule(100);
        long requestId2 = deadlineEngine.schedule(200);
        Consumer<Long> consumer = (Long x) -> System.out.println(x);
        int count = deadlineEngine.poll(1000,  consumer , 2);
        Assert.assertEquals(2, count);
    }

    @Test
    public void testPollEqualsDeadLine(){
        DeadlineEngine deadlineEngine = new DeadlineEngineImpl();
        long requestId1 = deadlineEngine.schedule(100);
        Consumer<Long> consumer = (Long x) -> System.out.println(x);
        int count = deadlineEngine.poll(100,  consumer , 1);
        Assert.assertEquals(1, count);
    }

    @Test
    public void testPollNotYetBreachedDeadLine(){
        DeadlineEngine deadlineEngine = new DeadlineEngineImpl();
        long requestId1 = deadlineEngine.schedule(101);
        Consumer<Long> consumer = (Long x) -> System.out.println(x);
        int count = deadlineEngine.poll(100,  consumer , 1);
        Assert.assertEquals(0, count);
    }

    @Test
    public void testPollTestConsumer(){
        DeadlineEngine deadlineEngine = new DeadlineEngineImpl();
        long requestId1 = deadlineEngine.schedule(100);
        int count = deadlineEngine.poll(1000,  consumer , 1);
        Mockito.verify(consumer).accept(consumerArgCaptor.capture());
        long val = consumerArgCaptor.getValue();
        Assert.assertEquals(1, count);
        Assert.assertEquals(1l, val);
    }


    @Test
    public void testCancel(){
        DeadlineEngine deadlineEngine = new DeadlineEngineImpl();
        long requestId1 = deadlineEngine.schedule(100);
        deadlineEngine.cancel(requestId1);
        int count = deadlineEngine.poll(1000,  consumer , 1);
        Assert.assertEquals(0, count);
    }

    @Test
    public void testSize() {
        DeadlineEngine deadlineEngine = new DeadlineEngineImpl();
        long requestId1 = deadlineEngine.schedule(100);
        long requestId2 = deadlineEngine.schedule(100);
        int size = deadlineEngine.size();
        Assert.assertEquals(2, size);
    }
}
