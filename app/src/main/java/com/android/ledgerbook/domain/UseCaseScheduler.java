package com.android.ledgerbook.domain;

/**
 * Interface for schedulers, see {@link UseCaseThreadPoolScheduler}.
 */
public interface UseCaseScheduler {
    void execute(Runnable runnable);
}
