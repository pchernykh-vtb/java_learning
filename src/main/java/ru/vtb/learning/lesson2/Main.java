package ru.vtb.learning.lesson2;

public class Main {
    public static void main(String[] args) {
        Fractionable fraction = new Fraction(1, 2);
        System.out.println(fraction.doubleValue());

        fraction = Utils.cache(fraction);
        System.out.println(fraction.doubleValue());
        fraction.setNum(2);
        System.out.println(fraction.doubleValue());
        System.out.println("Should be cached:");
        System.out.println(fraction.doubleValue());
    }
}
