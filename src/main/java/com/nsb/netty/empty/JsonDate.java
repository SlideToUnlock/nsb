package com.nsb.netty.empty;

import org.json.JSONObject;

/**
 * @Author:langxy
 * @date 创建时间：2017/12/26 13:48
 */
public class JsonDate {

    public String mac;
    public String ip;
    public String command;
    public JSONObject process;

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public JSONObject getProcess() {
        return process;
    }

    public void setProcess(JSONObject process) {
        this.process = process;
    }
}