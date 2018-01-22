package com.nsb.vo;

import java.util.List;

/**
 * @Author:langxy
 * @date 创建时间：2018/1/8 15:03
 */
public class CommandInfoVo {

    private String mac;
    private String command;
    private String status;
    private List processList;

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List getProcessList() {
        return processList;
    }

    public void setProcessList(List processList) {
        this.processList = processList;
    }
}
