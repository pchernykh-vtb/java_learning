package ru.vtb.learning;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AccountSnapshotTest {
    @Test
    public void accountSnapshotIsEncapsulated() {
        Account acc1 = new Account("Максим");
        acc1.setCurrencyValue("RUR", 10);
        AccountSnapshot accSnap1 = acc1.save();
        AccountSnapshot accSnap2 = acc1.save();
        // Ссылки должны быть различны - это значит, что возвращается новый объект, а не ссылка на внутрений.
        Assertions.assertNotSame(accSnap1, accSnap2);
        Assertions.assertNotSame(accSnap1.getCurrencyList(), accSnap2.getCurrencyList());
    }
}