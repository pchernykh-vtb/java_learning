package ru.vtb.learning.lesson2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CachingProxyTest {
    @Test
    void rightDoubleValue() {
        Fractionable fraction = new Fraction(1, 2);
        fraction = Utils.cache(fraction);
        Assertions.assertEquals(0.5, fraction.doubleValue());
    }

    @Test
    void cacheMissOnNumSet() {
        Fractionable originalFraction = new Fraction(1, 2);
        Fractionable cachedFraction = Utils.cache(originalFraction);

        cachedFraction.doubleValue();
        originalFraction.setNum(2);     // Замена числителя мимо прокси
        Assertions.assertEquals(0.5, cachedFraction.doubleValue()); // В прокси всё ещё 0.5
    }

    @Test
    void cacheMissOnDenomSet() {
        Fractionable originalFraction = new Fraction(1, 2);
        Fractionable cachedFraction = Utils.cache(originalFraction);

        cachedFraction.doubleValue();
        originalFraction.setDenum(1);     // Замена знаменателя мимо прокси
        Assertions.assertEquals(0.5, cachedFraction.doubleValue()); // В прокси всё ещё 0.5
    }
}
