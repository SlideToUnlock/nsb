package com.nsb.netty.server;

import com.google.gson.Gson;
import com.nsb.commons.Const;
import com.nsb.dao.InformationMapper;
import com.nsb.dao.ProcessMapper;
import com.nsb.pojo.Information;
import com.nsb.netty.empty.JsonDate;
import com.nsb.pojo.Process;
import com.nsb.netty.util.DisRoom;
import com.nsb.netty.util.GetIp;
import com.nsb.netty.util.MapUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Iterator;

/**
 * @Author:langxy
 * @date 创建时间：2018/1/4 19:57
 */
@Component
@Sharable
public class NettyHandler extends ChannelInboundHandlerAdapter {

    @Autowired
    private InformationMapper informationMapper;

    @Autowired
    private ProcessMapper processMapper;

    @Autowired
    private DisRoom disRoom;

    @Autowired
    private GetIp getIp;

    private static final String MESSAGE = "Heartbeat";

    private static final ByteBuf SHUTDOWN_ACTION = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("Shutdown",
            CharsetUtil.UTF_8));

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        String ip = getIp.getIpByChannel(ctx.channel());
        String mac = informationMapper.selectMacByIp(ip);
        informationMapper.updateStatusByMac(mac, Const.Status.SHUTDOWN);
        MapUtil.channelMap.remove(mac);
        System.out.println(mac+"已断开连接");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg.toString().equals(MESSAGE)){
            System.out.println(ctx.channel().remoteAddress() + "->Server :" + msg.toString());
        }else {
            Gson gson = new Gson();
            JsonDate jsonDate = gson.fromJson(msg.toString(), JsonDate.class);
            String ip = jsonDate.getIp();
            String mac = jsonDate.getMac();
            String command = jsonDate.getCommand();
            if (ip == null && mac == null){
                JSONObject jsonObject = new JSONObject(msg.toString());
                Iterator iterator = jsonObject.keys();
                mac = informationMapper.selectMacByIp(getIp.getIpByChannel(ctx.channel()));
                while (iterator.hasNext()){
                    Process process = new Process();
                    String key = (String) iterator.next();
                    String value = jsonObject.getString(key);
                    process.setMac(mac);
                    process.setProcess(value);
                    int rowCount = processMapper.checkProcess(mac, value);
                    if (rowCount == 0){
                        processMapper.insert(process);
                    }
                }
            }else {
                Information information = new Information();
                information.setMac(mac);
                information.setIp(ip);
                information.setStatus(Const.Status.START);
                information.setRoomId(disRoom.disComputerRoom(ip));
                if (StringUtils.isNoneBlank(command)){
                    ctx.channel().writeAndFlush(SHUTDOWN_ACTION);
                    ip = getIp.getIpByChannel(ctx.channel());
                    mac = informationMapper.selectMacByIp(ip);
                    MapUtil.channelMap.remove(mac);
                    information.setStatus(Const.Status.SHUTDOWN);
                    informationMapper.updateStatusByMac(mac, Const.Status.SHUTDOWN);
                    processMapper.deleteProcessByMac(mac);
                } else {
                    int rowCount;
                    rowCount = informationMapper.checkMac(mac);
                    if (rowCount > 0){
                        informationMapper.updateStatusByMac(mac, Const.Status.START);
                    }else {
                        informationMapper.insert(information);
                    }
                    MapUtil.channelMap.put(mac, ctx.channel());
                }
            }
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.READER_IDLE) {
                throw new Exception("idle exception");
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
