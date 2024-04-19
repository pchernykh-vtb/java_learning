package ru.vtb.learning.lesson1;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Account {
    private String clientName;
    private final HashMap<String, Integer> currencyList = new HashMap<>();
    private final History history;

    public Account (String clientName) {
        history = new History();
        setClientName(clientName);
    }

    public String getClientName() {return clientName;}

    public void setClientName(String clientName) {
        validateClientName(clientName);
        this.clientName = clientName;
        history.add(x->x.setClientName(clientName));
    }

    public Integer getCurrencyValue(String currency) {
        return currencyList.get(currency);
    }

    public void setCurrencyValue(String currency, Integer count) {
        validateCurrencyValue(currency, count);

        currencyList.put(currency, count);
        history.add(x->x.setCurrencyValue(currency, count));
    }

    public boolean checkCanUndo() {
        return (history.size() > 1);    // Один элемент должен всегда оставаться
        // - это инициализация имени клиента в конструкторе.
    }

    // Undo реализован откатом объекта к исходному состоянию и повторением истории его изменений, кроме последнего
    // действия.
    public void undo() {
        if (!checkCanUndo()) {
            throw new IllegalStateException("Account is in initial state. Can't undo.");
        }
        history.removeLast();   //Кроме последнего действия.
        currencyList.clear();
        int undoStepsCount = history.size();    // Сколько всего действий на данный момент в истории.
        // Повтор каждого действия дополнит историю с конца. Чтобы не зациклиться, выполним столько шагов,
        // сколько их в истории на начало выполнения.
        for (int i = 1; i <= undoStepsCount; i++) {
            // Выполняемый шаг убираем из истории. Его "копия" вернётся в историю при выполнении.
            history.removeFirst().modify(this);
        }
    }

    @Override
    public String toString() {
        return clientName + " " + currencyList + " history size = " + history.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account account)) return false;
        return Objects.equals(clientName, account.clientName) && Objects.equals(currencyList, account.currencyList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientName, currencyList);
    }

    public HashMap<String, Integer> getCurrencyList() {
        return (HashMap<String, Integer>) currencyList.clone();
    }

    public AccountSnapshot save() {
        return new AccountSnapshot(this);
    }

    public void load(AccountSnapshot accountSnapshot) {
        String clientName = accountSnapshot.getClientName();
        this.validateClientName(clientName);
        this.clientName = clientName;

        this.currencyList.clear();
        HashMap<String, Integer> currencyList = accountSnapshot.getCurrencyList();
        currencyList.forEach((c, v) -> {
            validateCurrencyValue(c, v);
            this.currencyList.put(c, v);
        });
    }

    private void validateClientName(String clientName) {
        if (clientName == null || clientName.isBlank()) {
            throw new IllegalArgumentException("Client name must be not empty");
        }
    }

    private static void validateCurrencyValue(String currency, Integer count) {
        if (currency == null || currency.isBlank()) {
            throw new IllegalArgumentException("Currency must be not empty");
        }
        if (count < 0) {
            throw new IllegalArgumentException("Currency count should be not negative");
        }

        List<String> rightCurrencyCodes = Arrays.asList("RUR", "USD", "EUR");   // Список допустимых кодов валюты.
        if (!rightCurrencyCodes.contains(currency)) {
            throw new IllegalArgumentException("Currency is not from valid list");
        }
    }
}