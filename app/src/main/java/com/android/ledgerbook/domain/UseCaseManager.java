package com.android.ledgerbook.domain;

import com.android.ledgerbook.models.User;
import com.android.ledgerbook.storage.RepositoriesManager;

public class UseCaseManager {

    protected final UseCaseScheduler useCaseScheduler;
    protected final RepositoriesManager repositoriesManager;

    public UseCaseManager(UseCaseScheduler useCaseScheduler, RepositoriesManager repositoriesManager) {
        this.useCaseScheduler = useCaseScheduler;
        this.repositoriesManager = repositoriesManager;
    }

    public void execute(UseCaseHandler handler) {
        handler.setActiveState(true);
        useCaseScheduler.execute(handler);
    }

    public UseCaseHandler<User> initApp() {
        return new SingleRequestResponseHandler<>(repositoriesManager::getUser);
    }

    public void logout() {
        repositoriesManager.logout();
        User.resetInstance();
    }
}
