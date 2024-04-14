package ru.vtb.learning;

import java.util.ArrayDeque;

public class History {
    private final ArrayDeque<Modification> history = new ArrayDeque<>();

    public void add(Modification modification) {
        history.addLast(modification);
    }

    public int size() {
        return history.size();
    }

    public void removeLast() {
        history.removeLast();
    }

    public Modification removeFirst() {
        return history.removeFirst();
    }
}