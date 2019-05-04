package com.android.ledgerbook.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustomError<T> {
    int errorCode;
    private T errorObj;
    @SerializedName("detail")
    @Expose
    private String detail;
    @SerializedName("error_code")
    @Expose
    private String psErrorCode;
    @SerializedName("errors")
    @Expose
    private String errors;

    public CustomError(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public T getErrorObj() {
        return errorObj;
    }

    public void setErrorObj(T errorObj) {
        this.errorObj = errorObj;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getPsErrorCode() {
        return psErrorCode;
    }

    public void setPsErrorCode(String psErrorCode) {
        this.psErrorCode = psErrorCode;
    }

    public String getErrors() {
        return errors;
    }

    public void setErrors(String errors) {
        this.errors = errors;
    }
}
