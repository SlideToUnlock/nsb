package com.nsb.commons;

/**
 * @Author:langxy
 * @date 创建时间：2018/1/5 11:26
 */
public class Const {

    public static final String USER = "user";

    public interface Status{
        String START = "true";
        String SHUTDOWN = "false";
    }

    public interface Command{
        String SHUTDOWN = "shutdown";
        String GETPROCESS = "getProcess";
    }

    public interface Role{
        int ROLE_CUSTOMER = 0;  //普通用户
        int ROLE_ADMIN = 1;     //管理员用户
    }
}
