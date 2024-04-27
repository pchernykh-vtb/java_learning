package ru.vtb.learning.lesson3;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

public class Utils {
    private static List<WeakReference> caches = new ArrayList<>();  //Список созданных прокси для объектов.
    private static Thread gc = null;

    public static <T> T cache(T objectIncome) {
        T cachingObj = (T) Proxy.newProxyInstance(
                objectIncome.getClass().getClassLoader(),
                objectIncome.getClass().getInterfaces(),
                new CachingProxy(objectIncome)
        );

        caches.add(new WeakReference(cachingObj));

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
            System.out.println("CacheGarbageCollector");
            for (; !Thread.interrupted(); ) {
                for (WeakReference cacheWeakObj : caches) {
//                    System.out.println("cacheWeakObj = " + cacheWeakObj);
//                    System.out.println("cacheWeakObj.get() = " + cacheWeakObj.get());
//                    System.out.println("cacheWeakObj.get().getClass() = " + cacheWeakObj.get().getClass());
//                    for(Method meth : cacheWeakObj.get().getClass().getDeclaredMethods()) {
//                        System.out.println("  " + meth.getName());
//                    }
//                    for(Field field : cacheWeakObj.get().getClass().getDeclaredFields()) {
//                        System.out.println("  " + field.getName() + " " + field.getType());
//                    }
                    CacheClearable cacheObj = (CacheClearable) cacheWeakObj.get();
                    if (cacheObj != null) {
                        cacheObj.removeOldResults();
                    }
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException exc) {
                    return;
                }
            }
        }
    }
}
