package ru.vtb.learning.lesson3;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CachingProxy implements InvocationHandler, CacheClearable {
    private Object originalObject;
    private CacheState state = new CacheState();
    private Map<CacheState, CacheResult> results = new ConcurrentHashMap<>();

    public CachingProxy(Object originalObject) {
        this.originalObject = originalObject;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Method originalMethod = originalObject.getClass().getMethod(method.getName(), method.getParameterTypes());

        if (originalMethod.isAnnotationPresent(Mutator.class)) {
            if (!state.state.containsKey(method) || !Arrays.deepEquals(state.state.get(method), args)) {
                state.state.put(method, args);
            }
        }
        if (originalMethod.isAnnotationPresent(Cache.class)) {
            // Ключом кэша будем считать сочетание состояние объекта и вызванного кэшированного метода
            // (с учётом его параметров). Состояние в свою очередь - это набор вызванных мутаторов с их параметрами.
            // Там и там метод + параметры, поэтому объединим состояние и вызванный в одной мапе и будем считать её ключом.
            // Считаем, что на методе не может быть одновременно @Cache и @Mutator, то есть объединение не затрёт значения.
            CacheState key = new CacheState(state); // Устроит и поверхностное копирование.
            key.state.put(method, args);  // К состоянию добавили кэшируемый метод, получили ключ к кэшу.

            Cache cacheAnnotation = originalMethod.getAnnotation(Cache.class);

            CacheResult result = null;
            result = results.get(key);
            if (result == null) {
                result = new CacheResult(method.invoke(originalObject, args), Instant.now().plusMillis(cacheAnnotation.lifetime()));
                results.put(key, result);
            } else {
                // Обновляем метку времени.
                result.setLiveUntil(Instant.now().plusMillis(cacheAnnotation.lifetime()));
                results.put(key, result);
            }

            return result.getResult();
        } else {
            return method.invoke(originalObject, args);
        }
    }

    @Override
    public void removeOldResults () {
        for (var result: results.entrySet()) {
            if (result.getValue().getLiveUntil().isBefore(Instant.now())) {
                results.remove(result.getKey());
            }
        }
    }

    private class CacheState {
        Map<Method, Object[]> state;

        public CacheState() {
            this.state = new HashMap<>();
        }
        public CacheState(CacheState cacheState) {
            this.state = new HashMap<>(cacheState.state);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof CacheState cacheState)) return false;

            // Сравнение двух мап через глубокое сравнение значений.
            CacheState objState = (CacheState) o;
            if (!state.keySet().equals(objState.state.keySet())) return false;
            for (var entry: state.entrySet()) {
                if (!Arrays.deepEquals(entry.getValue(), objState.state.get(entry.getKey()))) return false;
            }

            return true;
        }

        @Override
        public int hashCode() {
            int res = 0;
            for (var entry: state.entrySet()) {
                res = 31 * res + entry.getKey().hashCode();
                res = 31 * res + Arrays.hashCode(entry.getValue());
            }
            return res;
        }

        @Override
        public String toString() {
            String res = "CacheState{";

            for (var entry: state.entrySet()) {
                res += "\n  " + entry.getKey() + " = " + Arrays.toString(entry.getValue());
            }
            res += "\n}";

            return res;
        }
    }
}
