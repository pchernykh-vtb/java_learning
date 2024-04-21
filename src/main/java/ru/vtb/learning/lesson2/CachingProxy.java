package ru.vtb.learning.lesson2;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class CachingProxy implements InvocationHandler {
    private Object originalObject;
    private boolean cached = false;
    private Object cachedValue = 0.0;

    public CachingProxy(Object originalObject) {
        this.originalObject = originalObject;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Method originalMethod = originalObject.getClass().getMethod(method.getName(), method.getParameterTypes());

        if (originalMethod.isAnnotationPresent(Mutator.class)) {
            System.out.println(originalMethod.getName() + " have @Mutator");
            cached = false;
        }
        if (originalMethod.isAnnotationPresent(Cache.class)) {
            System.out.println(originalMethod.getName() + " have @Cache");
            if (!cached) {
                cachedValue = method.invoke(originalObject, args);
                cached = true;
            }
        }
        return cachedValue;
    }
}
