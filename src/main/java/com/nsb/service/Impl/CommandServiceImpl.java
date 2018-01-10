package com.nsb.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nsb.commons.Const;
import com.nsb.commons.ResponseCode;
import com.nsb.commons.ServerResponse;
import com.nsb.dao.InformationMapper;
import com.nsb.dao.ProcessMapper;
import com.nsb.netty.util.MapUtil;
import com.nsb.pojo.Information;
import com.nsb.service.ICommandService;
import com.nsb.vo.CommandInfoVo;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author:langxy
 * @date 创建时间：2018/1/8 14:53
 */
@Service("iCommandService")
public class CommandServiceImpl implements ICommandService {

    @Autowired
    private InformationMapper informationMapper;

    @Autowired
    private ProcessMapper processMapper;

    private static final ByteBuf SHUTDOWN_ACTION = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("Shutdown",
            CharsetUtil.UTF_8));

    private static final ByteBuf GET_PROCESS_ACTION = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("getProcess",
            CharsetUtil.UTF_8));

    public ServerResponse activeCommandShutdown(String mac, String roomId, int pageNum, int pageSize){

        if (StringUtils.isNotBlank(roomId) && StringUtils.isNotBlank(mac)){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ARGUMENT_ERROR.getCode(), ResponseCode.ARGUMENT_ERROR.getDesc());
        }

        if (StringUtils.isBlank(roomId) && StringUtils.isNotBlank(mac)){
            if (informationMapper.checkStatus(mac, Const.Status.START) == 0){
                return ServerResponse.createByErrorMessage("当前机器为关机状态");
            }
            CommandInfoVo commandInfoVo = new CommandInfoVo();
            commandInfoVo.setMac(mac);
            commandInfoVo.setCommand(Const.Command.SHUTDOWN);
            commandInfoVo.setStatus("true");
            commandInfoVo.setProcessList(processMapper.getProcess(mac));
            informationMapper.updateStatusByMac(mac, Const.Status.SHUTDOWN);
            MapUtil.channelMap.remove(mac);
            MapUtil.channelMap.get(mac).writeAndFlush(SHUTDOWN_ACTION);
            return ServerResponse.createBySuccess("command", commandInfoVo);
        }
        if (StringUtils.isNotBlank(roomId) && StringUtils.isBlank(mac)){
            PageHelper.startPage(pageNum, pageSize);
            if (informationMapper.checkRoomId(roomId) == 0){
                return ServerResponse.createByErrorMessage("当前机房不存在");
            }
            if (informationMapper.checkStatusByRoomId(roomId, Const.Status.START) == 0){
                return ServerResponse.createByErrorMessage("当前机房机器已是关状态");
            }
            List<Information> informationList = informationMapper.getCommandInfo(roomId);
            if (informationList != null){
                List<CommandInfoVo> commandInfoVoList = shutdownHandler(informationList);
                PageHelper.startPage(pageNum, pageSize);
                PageInfo pageInfo = new PageInfo(commandInfoVoList);
                return ServerResponse.createBySuccess("command", pageInfo);
            }
            return ServerResponse.createByErrorMessage("当前机房机器已是关状态");
        }
        int rowCount = informationMapper.checkAll(Const.Status.START);
        if (rowCount > 0){

            List<Information> informationList = informationMapper.getMacByStatus(Const.Status.START);
            List<CommandInfoVo> commandInfoVoList = shutdownHandler(informationList);
            PageInfo pageInfo = new PageInfo(commandInfoVoList);
            return ServerResponse.createBySuccess("command", pageInfo);
        }
        return ServerResponse.createByErrorMessage("当前没有开机的机器");
    }

    public List<CommandInfoVo> shutdownHandler(List<Information> informationList){
        List<CommandInfoVo> commandInfoVoList = new ArrayList<>();
        for (Information information: informationList) {
            CommandInfoVo commandInfoVo = new CommandInfoVo();
            commandInfoVo.setMac(information.getMac());
            commandInfoVo.setCommand(Const.Command.SHUTDOWN);
            commandInfoVo.setStatus("true");
            commandInfoVo.setProcessList(processMapper.getProcess(information.getMac()));
            informationMapper.updateStatusByMac(information.getMac(), Const.Status.SHUTDOWN);
            commandInfoVoList.add(commandInfoVo);
            MapUtil.channelMap.get(information.getMac()).writeAndFlush(SHUTDOWN_ACTION);
            MapUtil.channelMap.remove(information.getMac());
        }
        return commandInfoVoList;
    }

    public ServerResponse activeCommandGetProcess(String mac, String roomId, int pageNum, int pageSize){
        if (StringUtils.isNotBlank(roomId) && StringUtils.isNotBlank(mac)){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ARGUMENT_ERROR.getCode(), ResponseCode.ARGUMENT_ERROR.getDesc());
        }
        if (StringUtils.isBlank(roomId) && StringUtils.isNotBlank(mac)){
            int rowCount = informationMapper.checkStatus(mac, Const.Status.START);
            if(rowCount == 0){
                return ServerResponse.createByErrorMessage("当前机器为关机状态");
            }
            CommandInfoVo commandInfoVo = new CommandInfoVo();
            commandInfoVo.setMac(mac);
            commandInfoVo.setCommand(Const.Command.GET_PROCESS);
            commandInfoVo.setStatus("true");
            commandInfoVo.setProcessList(processMapper.getProcess(mac));
            MapUtil.channelMap.get(mac).writeAndFlush(GET_PROCESS_ACTION);
            return ServerResponse.createBySuccess("command", commandInfoVo);
        }
        if (StringUtils.isNotBlank(roomId) && StringUtils.isBlank(mac)){
            if (informationMapper.checkRoomId(roomId) == 0){
                return ServerResponse.createByErrorMessage("当前机房不存在");
            }
            if (informationMapper.checkStatusByRoomId(roomId, Const.Status.START) == 0){
                return ServerResponse.createByErrorMessage("当前机房机器已是关机状态");
            }

            List<Information> informationList = informationMapper.getCommandInfo(roomId);
            List<CommandInfoVo> commandInfoVoList = getProcessHandler(informationList);
            PageHelper.startPage(pageNum, pageSize);
            PageInfo pageInfo = new PageInfo(commandInfoVoList);
            return ServerResponse.createBySuccess("command", pageInfo);
        }
        int rowCount = informationMapper.checkAll(Const.Status.START);
        if (rowCount > 0){

            List<Information> informationList = informationMapper.getMacByStatus(Const.Status.START);
            List<CommandInfoVo> commandInfoVoList = getProcessHandler(informationList);
            PageHelper.startPage(pageNum, pageSize);
            PageInfo pageInfo = new PageInfo(commandInfoVoList);
            return ServerResponse.createBySuccess("command", pageInfo);
        }
        return ServerResponse.createBySuccessMessage("当前没有开机的机器");
    }

    public List<CommandInfoVo> getProcessHandler(List<Information> informationList){
        List<CommandInfoVo> commandInfoVoList = new ArrayList<>();
        for (Information information: informationList) {
            CommandInfoVo commandInfoVo = new CommandInfoVo();
            commandInfoVo.setMac(information.getMac());
            commandInfoVo.setCommand(Const.Command.GET_PROCESS);
            commandInfoVo.setStatus("true");
            commandInfoVo.setProcessList(processMapper.getProcess(information.getMac()));
            commandInfoVoList.add(commandInfoVo);
            MapUtil.channelMap.get(information.getMac()).writeAndFlush(GET_PROCESS_ACTION);
        }
        return commandInfoVoList;
    }
}