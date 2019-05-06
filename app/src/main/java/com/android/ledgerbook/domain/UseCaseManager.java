package com.android.ledgerbook.domain;

import com.android.ledgerbook.models.CreateBookRequest;
import com.android.ledgerbook.models.CreateBookResponse;
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

    public void logout() {
        repositoriesManager.logout();
        User.resetInstance();
    }

    public UseCaseHandler<User> initApp() {
        return new SingleRequestResponseHandler<>(repositoriesManager::getUser);
    }

    public UseCaseHandler<CreateBookResponse> createBook(CreateBookRequest request) {
        return new SingleRequestResponseHandler<>(request, repositoriesManager::createBook);
    }
}
