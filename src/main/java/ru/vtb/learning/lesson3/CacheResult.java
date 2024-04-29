package ru.vtb.learning.lesson3;

import java.time.Instant;

public class CacheResult {
    private Object result;
    private Instant liveUntil;

    public CacheResult(Object result, Instant liveUntil) {
        this.result = result;
        this.liveUntil = liveUntil;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Instant getLiveUntil() {
        return liveUntil;
    }

    public void setLiveUntil(Instant liveUntil) {
        this.liveUntil = liveUntil;
    }
}
