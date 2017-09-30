package com.fss.controller.vo;


public class JsonResultVO {
    public final static String SUCCESS="success";
    public final static String FAILURE="failure";
    private String status;
    private String message;
    private String error;

    public JsonResultVO(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public JsonResultVO(String status, String message, String error) {
        this.status = status;
        this.message = message;
        this.error = error;
    }

    public JsonResultVO(String status) {
        this.status=status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
