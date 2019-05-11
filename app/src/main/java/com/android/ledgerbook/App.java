package com.android.ledgerbook;

import android.app.Application;
import android.support.annotation.NonNull;

import com.android.ledgerbook.domain.UseCaseManager;
import com.android.ledgerbook.domain.UseCaseThreadPoolScheduler;
import com.android.ledgerbook.storage.RepositoriesManager;
import com.android.ledgerbook.storage.local.AppPreferences;
import com.android.ledgerbook.storage.remote.RemoteRepositoryConfig;
import com.android.ledgerbook.utils.LocaleUtils;
import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

public class App extends Application {
    private AppPreferences preferences;
    protected UseCaseManager useCaseManager;
    private RepositoriesManager repositoriesManager;
    private String language;

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    protected void init() {
        preferences = new AppPreferences(this);
        initFabric();
    }

    public UseCaseManager getUseCaseManager() {
        if (useCaseManager == null) {
            useCaseManager = new UseCaseManager(new UseCaseThreadPoolScheduler(),
                    getRepositoriesManager());

        }
        return useCaseManager;
    }

    public RepositoriesManager getRepositoriesManager() {
        if (repositoriesManager == null) {
            repositoriesManager = new RepositoriesManager(this,
                    new RemoteRepositoryConfig(this));
        }
        return repositoriesManager;
    }

    @NonNull
    public AppPreferences getPreferences() {
        return preferences;
    }

    protected void initFabric() {
        Fabric.with(this, new Crashlytics());
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(@LocaleUtils.LanguageCode String language) {
        this.language = language;
    }
}
