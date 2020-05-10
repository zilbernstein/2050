package ru.digipeople.ui.mvp;

/**
 * @author Kashonkov Nikita
 */
public interface KeepAliveProvider {
    boolean keepAlive(boolean parentKeepAlive);
}
