package com.nsb.netty.util;

import org.springframework.stereotype.Component;

/**
 * @Author:langxy
 * @date 创建时间：2018/1/5 11:32
 */
@Component
public class DisRoom {

    private String roomId;

    public String disComputerRoom(String ip){
        Integer ipItem = Integer.valueOf(ip.substring(6,8));
        switch (ipItem){
            case 11:
                roomId = "1";
                break;
            case 12:
                roomId = "2";
                break;
            case 13:
                roomId = "3";
                break;
            case 15:
                roomId = "5";
                break;
            case 16:
                roomId = "6";
                break;
            case 17:
                roomId = "7";
                break;
            default:
                roomId = "0";
                break;
        }
        return roomId;
    }
}
