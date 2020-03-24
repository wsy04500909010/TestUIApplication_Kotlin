package com.wsy.testuiapplication.bean;

/**
 * Created by WSY on 2020/3/24.
 */
public class MessageBean {
    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "MessageBean{" + "code=" + code + ", message='" + message + '\'' + '}';
    }
}
