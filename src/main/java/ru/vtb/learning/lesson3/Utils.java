package ru.vtb.learning.lesson3;

import java.lang.ref.WeakReference;
import java.lang.reflect.Proxy;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class Utils {
    private static final List<WeakReference<CachingProxy>> caches = new ArrayList<>();  //Список созданных прокси для объектов.
    private static Thread gc = null;
    private static final Object lockObj = new Object();
    public static <T> T cache(T objectIncome) {
        CachingProxy cachingProxy = new CachingProxy(objectIncome);
        T cachingObj = (T) Proxy.newProxyInstance(
                objectIncome.getClass().getClassLoader(),
                objectIncome.getClass().getInterfaces(),
                cachingProxy
        );

        caches.add(new WeakReference<>(cachingProxy));

        if (gc == null) {
            System.out.println("Creating CacheGarbageCollector");
            gc = new Thread(new CacheGarbageCollector());
            gc.setDaemon(true);
            gc.start();
        }

        return cachingObj;
    }

    public static class CacheGarbageCollector implements Runnable {
        public void run() {
            while (!Thread.interrupted()) {
                Instant closestRemove = Instant.MAX;
                for (WeakReference<CachingProxy> cacheWeakObj : caches) {
                    Instant liveUntil = null;
                    CacheClearable cacheObj = cacheWeakObj.get();
                    if (cacheObj != null) {
                        liveUntil = cacheObj.removeOldResults();
                        // Сохраним минимальную отметку времени жизни кэшей.
                        closestRemove = liveUntil == null ? closestRemove : (closestRemove.isAfter(liveUntil) ? liveUntil : closestRemove);
                    } else {
                        // Объекта больше нет, убираем его и из списка.
                        caches.remove(cacheWeakObj);
                    }
                }
                if (closestRemove == Instant.MAX) {
                    // Если кэши опустели, чистить больше нечего, поток усыпляем.
                    Object lock = getLockObject();
                    synchronized (lock) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                } else {
                    try {
                        // Подождём пока не придёт время ближайшей чистки (минимум по всем кэшам всех объектов).
                        Thread.sleep(Duration.between(Instant.now(), closestRemove).toMillis());
                    } catch (InterruptedException exc) {
                        return;
                    }
                }
            }
        }
    }

    public static Object getLockObject() {
        return lockObj;
    }
}
