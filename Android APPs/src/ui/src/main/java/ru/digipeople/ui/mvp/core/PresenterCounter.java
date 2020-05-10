package ru.digipeople.ui.mvp.core;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

/**
 * Счетчик используемых презентеров.
 *
 * @author Kashonkov Nikita
 */
public class PresenterCounter {

    private final Map<String, Integer> connections = new HashMap<>();

    @Inject
    public PresenterCounter() {

    }
    /**
     * увеличить счетчик на 1
     */
    public void incrementCounter(@NonNull String tag) {
        Integer currentCount = connections.get(tag);
        if (currentCount == null) {
            currentCount = 1;
            connections.put(tag, currentCount);
            return;
        }
        connections.put(tag, currentCount + 1);
    }
    /**
     * уменьшить счетчик на 1
     */
    public boolean decrementCounter(@NonNull String tag) {
        Integer currentCount = connections.get(tag);
        if (currentCount == null) {
            throw new IllegalStateException("Invalid connections count");
        }
        if (currentCount == 1) {
            connections.remove(tag);
            return true;
        }
        connections.put(tag, currentCount - 1);
        return false;
    }
}
