package com.nsb.commons;

/**
 * @Author:langxy
 * @date 创建时间：2018/1/5 10:52
 */
public enum ResponseCode {
    SUCCESS(0,"SUCCESS"),
    ERROR(1,"ERROR"),
    NEED_LOGIN(10,"NEED_LOGIN"),
    NEED_PERMISSION(20,"NEED_PERMISSION"),
    ARGUMENT_ERROR(30,"ARGUMENT_ERROR");

    private final int code;
    private final String desc;

    ResponseCode(int code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}

