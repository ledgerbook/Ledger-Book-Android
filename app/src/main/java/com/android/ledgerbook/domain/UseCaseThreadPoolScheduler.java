package com.android.ledgerbook.domain;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * See also {@link Executors} for a list of factory methods to create common
 * {@link ExecutorService}s for different scenarios.
 */
public class UseCaseThreadPoolScheduler implements UseCaseScheduler {
    ExecutorService executorService;

    public UseCaseThreadPoolScheduler() {
        executorService = Executors.newCachedThreadPool();
    }

    @Override
    public void execute(Runnable runnable) {
        executorService.execute(runnable);
    }
}
