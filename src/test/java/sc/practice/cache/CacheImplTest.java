package sc.practice.cache;

import org.junit.Assert;
import org.junit.Test;

public class CacheImplTest {

    @Test
    public void testGetUsingFunction(){
        Cache<Integer, Integer> impl = new CacheImpl<>( k -> 100*k, 100);
        int result = impl.get(1);
        Assert.assertEquals(100, result);
    }

    @Test
    public void testGet(){
        Cache<Integer, Integer> impl = new CacheImpl<>( k -> 100*k, 100);
        int result = impl.get(1);
        result = impl.get(1);
        Assert.assertEquals(100, result);
    }

    @Test
    public void testGetMultipleGets(){
        Cache<Integer, Integer> impl = new CacheImpl<>( k -> 100*k, 100);
        impl.get(1);
        int result = impl.get(2);
        Assert.assertEquals(200, result);
    }

    @Test
    public void testGetDescreaseConcurrencyLevel(){
        Cache<Integer, Integer> impl = new CacheImpl<>( k -> 100*k, 1);
        impl.get(1);
        int result = impl.get(2);
        Assert.assertEquals(200, result);
    }
}
