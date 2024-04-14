package ru.vtb.learning;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AccountTest {
    @Test
    public void accountTestSuccess() {
        String clientName = "Максим";
        Account acc1 = new Account(clientName);
        Assertions.assertEquals(clientName, acc1.getClientName());

        Assertions.assertNull(acc1.getCurrencyValue("RUR"));

        String currency = "RUR";
        Integer count = 1;
        acc1.setCurrencyValue(currency, count);
        Assertions.assertEquals(count, acc1.getCurrencyValue(currency));
    }

    @Test
    public void accountEmptyClientNameThrowException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Account(null));
    }

    @Test
    public void accountNegativeCurrencyCountThrowException() {
        String clientName = "Максим";
        Account acc1 = new Account(clientName);
        String currency = "RUR";
        Integer count = -1;
        Assertions.assertThrows(IllegalArgumentException.class, () -> acc1.setCurrencyValue(currency, count)
        );
    }

    @Test
    public void accountWrongCurrencyNameThrowException() {
        String clientName = "Максим";
        Account acc1 = new Account(clientName);
        String currency = "BYR";
        Integer count = 1;
        Assertions.assertThrows(IllegalArgumentException.class, () -> acc1.setCurrencyValue(currency, count)
        );
    }

    @Test
    public void accountEmptyCurrencyNameThrowException() {
        String clientName = "Максим";
        Account acc1 = new Account(clientName);
        String currency = "";
        Integer count = 1;
        Assertions.assertThrows(IllegalArgumentException.class, () -> acc1.setCurrencyValue(currency, count)
        );
    }

    @Test
    public void accountUndoSuccess() {
        Account expectedAcc = new Account("Козёл");
        Account acc = new Account("Козёл");
        acc.setCurrencyValue("RUR", 10);
        acc.setClientName("Осёл");
        acc.setCurrencyValue("USD", 5);
        acc.setCurrencyValue("RUR", 20);
        acc.setClientName("Косолапый мишка");

        acc.undo();
        acc.undo();
        acc.undo();
        acc.undo();
        acc.undo();

        Assertions.assertEquals(expectedAcc, acc);
    }

    @Test
    public void accountExtraUndoThrowException() {
        Account acc = new Account("Козёл");
        acc.setCurrencyValue("RUR", 10);

        Exception exception = Assertions.assertThrows(IllegalStateException.class, () -> {acc.undo(); acc.undo();});
        Assertions.assertEquals("Account is in initial state. Can't undo.", exception.getMessage());
    }

    @Test
    public void getCurrencyListIsEncapsulated() {
        Account acc = new Account("Козёл");
        acc.setCurrencyValue("RUR", 10);
        Assertions.assertNotSame(acc.getCurrencyList(), acc.getCurrencyList());
    }

    @Test
    public void saveSuccess() {
        Account acc1 = new Account("Максим");
        acc1.setCurrencyValue("RUR", 10);
        AccountSnapshot accSnap = acc1.save();

        Assertions.assertAll(
                () -> Assertions.assertEquals(acc1.getClientName(), accSnap.getClientName()),
                () -> Assertions.assertEquals(acc1.getCurrencyList(), accSnap.getCurrencyList())
        );
    }

    @Test
    public void loadSuccess() {
        Account acc1 = new Account("Максим");
        AccountSnapshot accSnap = acc1.save();
        acc1.setCurrencyValue("RUR", 10);
        acc1.load(accSnap);

        Assertions.assertAll(
                () -> Assertions.assertEquals(acc1.getClientName(), accSnap.getClientName()),
                () -> Assertions.assertEquals(acc1.getCurrencyList(), accSnap.getCurrencyList())
        );
    }
}