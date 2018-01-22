package com.nsb.service.Impl;

import com.google.common.base.Splitter;
import com.nsb.commons.ServerResponse;
import com.nsb.dao.InformationMapper;
import com.nsb.pojo.Information;
import com.nsb.service.IManageComputerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author:langxy
 * @date 创建时间：2018/1/8 23:36
 */
@Service("iManageComputerService")
public class ManageComputerServiceImpl implements IManageComputerService{

    @Autowired
    private InformationMapper informationMapper;

    public ServerResponse distribution(String macs, String roomId){
        List<String> macList = Splitter.on(",").splitToList(macs);
        for (String mac: macList) {
            Information information = new Information();
            information.setMac(mac);
            information.setRoomId(roomId);
            int rowCount = informationMapper.checkMac(mac);
            if (rowCount == 0){

                informationMapper.insertSelective(information);
            }
            informationMapper.updateRoomIdByMac(roomId, mac);
        }
        return ServerResponse.createBySuccessMessage("添加成功");
    }
}
