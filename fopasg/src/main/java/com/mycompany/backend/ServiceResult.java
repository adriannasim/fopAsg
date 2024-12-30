package com.mycompany.backend;

public class ServiceResult {
    private boolean successful;
    private Object object;
    private String message;

    public ServiceResult(boolean successful, Object object, String message) 
    {
        this.successful = successful;
        this.object = object;
        this.message = message;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public Object getReturnObject() {
        return object;
    }

    public String getReturnMessage() {
        return message;
    }
}
