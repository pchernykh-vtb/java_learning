package ru.vtb.learning;

public class Main {
    public static void main(String[] args) {
        Account acc = new Account("Козёл");
        System.out.println(acc);
        acc.setCurrencyValue("RUR", 10);
        System.out.println(acc);
        acc.setClientName("Осёл");
        System.out.println(acc);
        acc.setCurrencyValue("USD", 5);
        System.out.println(acc);
        acc.setCurrencyValue("RUR", 20);
        System.out.println(acc);
        acc.setClientName("Косолапый мишка");
        System.out.println(acc);

        System.out.println("undo:");
        acc.undo();
        System.out.println(acc);

        System.out.println("undo:");
        acc.undo();
        System.out.println(acc);

        System.out.println("undo:");
        acc.undo();
        System.out.println(acc);

        System.out.println("undo:");
        acc.undo();
        System.out.println(acc);

        System.out.println("undo:");
        acc.undo();
        System.out.println(acc);

        AccountSnapshot accSnap = acc.save();
        acc.setClientName("Косолапый мишка");
        System.out.println(acc);
        acc.load(accSnap);
        System.out.println(acc);
    }
}