package com.android.ledgerbook.utils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;

@Retention(RetentionPolicy.SOURCE)
@Target(METHOD)
public @interface ApiSuccess {
}
