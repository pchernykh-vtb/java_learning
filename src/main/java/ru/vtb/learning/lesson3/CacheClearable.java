package ru.vtb.learning.lesson3;

import java.time.Instant;

public interface CacheClearable {
    public Instant removeOldResults ();
}
