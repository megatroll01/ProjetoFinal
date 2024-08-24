package com.my.work.manager.myworkmanager_back.util;

import org.springframework.http.HttpStatus;

public class CustomResponse<T> {

    private T data;
    private int statusCode;

    public CustomResponse(T data, HttpStatus status) {
        this.data = data;
        this.statusCode = status.value();
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
