package ru.vtb.learning.lesson3;

public class Main {
    public static void main(String[] args) {
        Fractionable fraction = new Fraction(0, 0);

        fraction = Utils.cache(fraction);
        fraction.setNum(1);
        fraction.setDenum(2);
        System.out.println(fraction.doubleValue());
        System.out.println("Should be cached:");
        System.out.println(fraction.doubleValue());

        fraction.setNum(1);     // Мутатор, но с тем же входным значением.
        System.out.println("Should be cached:");
        System.out.println(fraction.doubleValue());

        fraction.setNum(2);     // Новое значение
        System.out.println(fraction.doubleValue());

        System.out.println("Should be cached:");
        fraction.setNum(1);     // Снова первое значение
        System.out.println(fraction.doubleValue());

        fraction.setDenum(4);   // Другой мутатор, новое значение
        System.out.println(fraction.doubleValue());

        fraction.setDenum(2);   // Возвращаемся.
        System.out.println("Should be cached:");
        System.out.println(fraction.doubleValue());
        try {
            System.out.println("Sleeping");
            Thread.sleep(2000);
        } catch (InterruptedException exc) {}
        System.out.println(fraction.doubleValue());
    }
}
