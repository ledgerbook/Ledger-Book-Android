package com.android.ledgerbook.storage;

import android.content.Context;

import com.android.ledgerbook.domain.CustomDataException;
import com.android.ledgerbook.domain.UseCaseHandler;
import com.android.ledgerbook.models.CustomError;
import com.android.ledgerbook.models.User;
import com.android.ledgerbook.storage.local.LocalDataRepository;
import com.android.ledgerbook.storage.remote.RemoteDataRepository;
import com.android.ledgerbook.storage.remote.RemoteRepositoryConfig;
import com.android.ledgerbook.utils.Constants;
import com.android.ledgerbook.utils.Functions;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class RepositoriesManager {
    final RemoteRepositoryConfig remoteRepositoryConfig;
    private final LocalDataRepository localDataRepository;
    private final RemoteDataRepository remoteDataRepository;
    protected final Context appContext;

    public RepositoriesManager(Context appContext, RemoteRepositoryConfig remoteConfig) {
        this.appContext = appContext;
        this.localDataRepository = new LocalDataRepository();
        this.remoteRepositoryConfig = remoteConfig;
        this.remoteDataRepository = remoteRepositoryConfig.getApiService();
    }

    protected <E, T> T executeCall(UseCaseHandler handler, E request, CallFunction<E, Call<T>> function) throws CustomDataException {
        return executeCall(handler, request, function.apply(request));
    }

    protected <T, R> T executeCall(UseCaseHandler handler, R errorResponseType, Call<T> call) throws CustomDataException {
        int errorCode = 0;

        try {
            if (handler != null) {
                if (handler.isCancelled()) {
                    throw new CustomDataException();
                }
                handler.registerRepositoryHandler(new RepositoryHandler().registerCall(call));
            }
            Response<T> response = call.execute();
            Map<String, String> params = new HashMap<>(1);
            if (response.isSuccessful()) {
                return response.body();
            } else {
                ResponseBody errorResponseBody = response.errorBody();
                CustomDataException exception = new CustomDataException();
                errorCode = response.code();
                if (errorResponseBody != null) {
                    CustomError error;
                    String errorJson = errorResponseBody.string();
                    Object errorObject = null;
                    if (errorResponseType != null) {
                        if (errorResponseType instanceof Type) {
                            errorObject = remoteRepositoryConfig.getErrorGsonInstance().fromJson(errorJson,
                                    (Type) (errorResponseType));
                        } else {
                            errorObject = remoteRepositoryConfig.getErrorGsonInstance().fromJson(errorJson,
                                    errorResponseType.getClass());
                        }
                    }

                    if (!(errorObject instanceof CustomError)) {
                        error = remoteRepositoryConfig.getErrorGsonInstance().fromJson(errorJson, CustomError.class);
                        error.setErrorObj(errorObject);
                    } else {
                        error = (CustomError) errorObject;
                    }
                    error.setErrorCode(errorCode);
                    exception.setCustomError(error);
                } else {
                    if (errorCode > 0) {
                        exception.setCustomError(new CustomError(errorCode));
                    } else {
                        errorCode = Functions.isOnline(appContext) ?
                                Constants.ERROR_UNEXPECTED : Constants.ERROR_NO_NETWORK;
                        exception.setCustomError(new CustomError(errorCode));
                    }
                }

                throw exception;
            }
        } catch (IOException e) {
            e.printStackTrace();
            CustomDataException exception = new CustomDataException();
            errorCode = Functions.isOnline(appContext) ?
                    Constants.ERROR_UNEXPECTED : Constants.ERROR_NO_NETWORK;
            exception.setCustomError(new CustomError(errorCode));
            throw exception;
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof CustomDataException) {
                throw e;
            } else {
                CustomDataException customDataException;
                customDataException = new CustomDataException();
                customDataException.setCustomError(new CustomError(errorCode));
                throw customDataException;
            }
        }
    }

    private interface CallFunction<T, R> {
        R apply(T params);
    }

    public void logout() {
        remoteRepositoryConfig.clearCache();
    }

    public User getUser(UseCaseHandler handler) throws CustomDataException {
        return executeCall(handler, null, remoteDataRepository.getUser());
    }
}
