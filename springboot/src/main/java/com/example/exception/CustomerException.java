package com.example.exception;

/**
 * 自定义异常
 * 运行时异常
 * 继承 RuntimeException
 */
public class CustomerException extends RuntimeException {
    private String code;
    private String msg;
//三段构造器 有参 少参 无参
    public CustomerException(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public CustomerException(String msg) {
        this.code = "500";
        this.msg = msg;
    }

    public CustomerException() {}

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}