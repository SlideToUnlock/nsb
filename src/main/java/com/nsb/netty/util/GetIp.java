package com.nsb.netty.util;

import io.netty.channel.Channel;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author:langxy
 * @date 创建时间：2018/1/5 13:47
 */
@Component
public class GetIp {

    private String ip;

    public String getIpByChannel(Channel channel){
        String reg = "/(.*?):";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(channel.remoteAddress().toString());
        while (m.find()) {
            ip = m.group(1);
        }
        return ip;
    }
}
