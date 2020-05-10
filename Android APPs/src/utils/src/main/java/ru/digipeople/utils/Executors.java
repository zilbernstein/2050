package ru.digipeople.utils;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import ru.digipeople.logger.Logger;
import ru.digipeople.logger.LoggerFactory;

/**
 * Фабричные методы для генерации {@link Executor}.
 *
 * @author Aleksandr Brazhkin
 */
public class Executors {

    /**
     * Возвращает логируемый {@link ExecutorService} c указанными параметрами.
     * Альтернатива {@link ThreadPoolExecutor}
     */
    public static ExecutorService newLoggableThreadPoolExecutor(final String logTag,
                                                                int corePoolSize,
                                                                int maximumPoolSize,
                                                                long keepAliveTime,
                                                                TimeUnit unit,
                                                                BlockingQueue<Runnable> workQueue,
                                                                ThreadFactory threadFactory) {
        return new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
                keepAliveTime, unit,
                workQueue,
                threadFactory) {
            private final Logger logger = LoggerFactory.getLogger(logTag);

            @Override
            protected void afterExecute(Runnable r, Throwable t) {
                super.afterExecute(r, t);
                Throwable throwable = obtainThrowable(r, t);
                if (throwable != null) {
                    logger.error("Task Error", t);
                }
            }
        };
    }

    /**
     * Возвращает логируемый {@link ExecutorService} c одним потоком.
     * Альтернатива {@link java.util.concurrent.Executors#newSingleThreadExecutor()}
     */
    public static ExecutorService newLoggableSingleThreadExecutor(final String logTag, ThreadFactory threadFactory) {
        return newLoggableThreadPoolExecutor(logTag, 1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(),
                threadFactory);
    }

    /**
     * Возвращает логируемый {@link ScheduledExecutorService} c одним потоком.
     * Альтернатива {@link java.util.concurrent.Executors#newSingleThreadScheduledExecutor()}
     */
    public static ScheduledExecutorService newLoggableSingleThreadScheduledExecutor(final String logTag, ThreadFactory threadFactory) {
        return new ScheduledThreadPoolExecutor(1, threadFactory) {
            private final Logger logger = LoggerFactory.getLogger(logTag);

            @Override
            protected void afterExecute(Runnable r, Throwable t) {
                super.afterExecute(r, t);
                Throwable throwable = obtainThrowable(r, t);
                if (throwable != null) {
                    logger.error("Task Error", t);
                }
            }
        };
    }

    private static Throwable obtainThrowable(Runnable r, Throwable t) {
        if (t == null
                && r instanceof Future<?>
                && ((Future<?>) r).isDone()) {
            try {
                ((Future<?>) r).get();
            } catch (CancellationException ce) {
                t = ce;
            } catch (ExecutionException ee) {
                t = ee.getCause();
            } catch (InterruptedException ie) {
                // ignore/reset
                Thread.currentThread().interrupt();
            }
        }
        return t;
    }
}
