package com.testtask.itprom.model;

public class ErrorMessage {

    protected String errorCode;

    protected String errorMsg;

    protected Integer status;

    protected String timestamp;

    public ErrorMessage() {
    }

    public ErrorMessage(String errorCode, String errorMsg, Integer status, String timestamp) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.status = status;
        this.timestamp = timestamp;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
