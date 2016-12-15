package com.br.pagpeg.retrofit.model;

/**
 * Created by brunolemgruber on 15/12/16.
 */

public class Charge {

    private String message;

    private boolean success;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
