package ru.vtb.learning;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.NoSuchElementException;

public class HistoryTest {
    @Test
    void addSuccess() {
        History history = new History();
        Assertions.assertEquals(0, history.size());
        history.add(x -> x.setClientName("Тест"));
        Assertions.assertEquals(1, history.size());
    }

    @Test
    void addnullThrowException() {
        History history = new History();
        Assertions.assertThrows(NullPointerException.class, () -> history.add(null));
    }

    @Test
    void removeLastSuccess() {
        History history = new History();
        Assertions.assertEquals(0, history.size());
        history.add(x -> x.setClientName("Тест"));
        Assertions.assertEquals(1, history.size());
        history.removeLast();
        Assertions.assertEquals(0, history.size());
    }

    @Test
    void emptyHistoryRemoveLastThrowException() {
        History history = new History();
        Assertions.assertEquals(0, history.size());
        Assertions.assertThrows(NoSuchElementException.class, history::removeLast);
    }

    @Test
    void RemoveFirstSuccess() {
        History history = new History();
        Assertions.assertEquals(0, history.size());
        history.add(x -> x.setClientName("Тест"));
        Assertions.assertEquals(1, history.size());
        history.removeFirst();
        Assertions.assertEquals(0, history.size());
    }

    @Test
    void emptyHistoryRemoveFirstThrowException() {
        History history = new History();
        Assertions.assertEquals(0, history.size());
        Assertions.assertThrows(NoSuchElementException.class, history::removeFirst);
    }
}
