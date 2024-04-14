package ru.vtb.learning;

import java.util.HashMap;
import java.util.Objects;

public class AccountSnapshot {
    private final String clientName;
    private final HashMap<String, Integer> currencyList;

    public AccountSnapshot(Account account) {
        clientName = account.getClientName();
        currencyList = account.getCurrencyList();
    }

    public String getClientName() {
        return clientName;
    }

    public HashMap<String, Integer> getCurrencyList() {
        return (HashMap<String, Integer>) currencyList.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccountSnapshot that)) return false;
        return Objects.equals(clientName, that.clientName) && Objects.equals(currencyList, that.currencyList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientName, currencyList);
    }
}
