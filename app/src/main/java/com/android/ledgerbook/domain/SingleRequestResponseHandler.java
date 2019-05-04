package com.android.ledgerbook.domain;

public class SingleRequestResponseHandler<T, R> extends UseCaseHandler<R> {
    private T input;
    private CustomHandlerFunction<T, R> handlerFunc;
    private CustomSimpleFunction<R> simpleFunc;

    SingleRequestResponseHandler(T input, CustomHandlerFunction<T, R> handlerFunc) {
        this.input = input;
        this.handlerFunc = handlerFunc;
    }

    SingleRequestResponseHandler(CustomSimpleFunction<R> simpleFunc) {
        this.simpleFunc = simpleFunc;
    }

    @Override
    public R runUseCase() throws CustomDataException {
        if (handlerFunc != null) {
            return handlerFunc.apply(this, input);
        } else {
            return simpleFunc.apply(this);
        }
    }

    public interface CustomHandlerFunction<T, R> {
        R apply(UseCaseHandler useCaseHandler, T params) throws CustomDataException;
    }

    public interface CustomSimpleFunction<R> {
        R apply(UseCaseHandler useCaseHandler) throws CustomDataException;
    }
}
