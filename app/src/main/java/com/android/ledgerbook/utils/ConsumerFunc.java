package com.android.ledgerbook.utils;

public interface ConsumerFunc<T> {
    void accept(T t);
}
