package com.nsb.vo;

import java.util.List;

/**
 * @Author:langxy
 * @date 创建时间：2018/1/8 15:02
 */
public class ComInfoVo {

    private String mac;
    private String ip;
    private String roomId;
    private String isBoot;
    private List processList;

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

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getIsBoot() {
        return isBoot;
    }

    public void setIsBoot(String isBoot) {
        this.isBoot = isBoot;
    }

    public List getProcessList() {
        return processList;
    }

    public void setProcessList(List processList) {
        this.processList = processList;
    }
}
