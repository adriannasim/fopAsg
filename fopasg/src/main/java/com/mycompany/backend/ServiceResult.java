package com.mycompany.backend;

public class ServiceResult {
    private Object object;
    private String message;

    public ServiceResult(Object object, String message) {
        this.object = object;
        this.message = message;
    }

    public Object getReturnObject() {
        return object;
    }

    public String getReturnMessage() {
        return message;
    }
}
