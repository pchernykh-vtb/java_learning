package ru.vtb.learning.lesson3;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CachingProxyTest {
    @Test
    void calculationInvokedSuccess() {
        int calculatorCount;
        FractionTest fractionTest = new FractionTest(0, 0);
        Fractionable fractionCache = Utils.cache(fractionTest);
        fractionCache.setNum(1);
        fractionCache.setDenum(2);

        calculatorCount = fractionTest.getCalculatorCount();
        fractionCache.doubleValue();
        // Счётчик вычислений увеличился.
        Assertions.assertEquals(calculatorCount + 1, fractionTest.getCalculatorCount());
    }

    @Test
    void calculationCachedSuccess() {
        int calculatorCount;
        FractionTest fractionTest = new FractionTest(0, 0);
        Fractionable fractionCache = Utils.cache(fractionTest);
        fractionCache.setNum(1);
        fractionCache.setDenum(2);

        fractionCache.doubleValue();
        calculatorCount = fractionTest.getCalculatorCount();
        fractionCache.doubleValue();
        // Счётчик вычислений не изменился.
        Assertions.assertEquals(calculatorCount, fractionTest.getCalculatorCount());
    }

    @Test
    void oldCacheUsedSuccess() {
        int calculatorCount;
        FractionTest fractionTest = new FractionTest(0, 0);
        Fractionable fractionCache = Utils.cache(fractionTest);
        // Устанавливаем одно состояние и кэшируем результат.
        fractionCache.setNum(1);
        fractionCache.setDenum(2);
        fractionCache.doubleValue();

        // Устанавливаем другое состояние и кэшируем результат.
        fractionCache.setNum(2);
        fractionCache.doubleValue();

        // Возвращаемся к первому состоянию и используем кэш.
        fractionCache.setNum(1);
        calculatorCount = fractionTest.getCalculatorCount();
        fractionCache.doubleValue();
        // Счётчик вычислений не изменился.
        Assertions.assertEquals(calculatorCount, fractionTest.getCalculatorCount());
    }

    @Test
    void cacheExpiredSuccess() {
        int calculatorCount;
        FractionTest fractionTest = new FractionTest(0, 0);
        Fractionable fractionCache = Utils.cache(fractionTest);
        fractionCache.setNum(1);
        fractionCache.setDenum(2);

        fractionCache.doubleValue();

        // Подождём установленное в аннотации время жизни, плюс запас сверху для гарантии.
        Cache cacheAnnotation;
        try {
            cacheAnnotation = fractionTest.getClass().getMethod("doubleValue").getAnnotation(Cache.class);
        } catch (NoSuchMethodException exc) {
            throw new IllegalStateException(fractionTest.getClass() + " have no 'doubleValue' method");
        }

        try {
            Thread.sleep(cacheAnnotation.lifetime() + 500);
        } catch (InterruptedException exc) {
            return;
        }

        calculatorCount = fractionTest.getCalculatorCount();
        fractionCache.doubleValue();
        // Счётчик вычислений увеличился.
        Assertions.assertEquals(calculatorCount + 1, fractionTest.getCalculatorCount());
    }
}
