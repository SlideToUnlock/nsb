package com.nsb.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nsb.commons.ServerResponse;
import com.nsb.dao.InformationMapper;
import com.nsb.dao.ProcessMapper;
import com.nsb.pojo.Information;
import com.nsb.service.IGetComInfoService;
import com.nsb.vo.ComInfoVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author:langxy
 * @date 创建时间：2018/1/8 9:41
 */
@Service("iGetComInfoService")
public class GetComInfoServiceImpl implements IGetComInfoService{

    @Autowired
    private InformationMapper informationMapper;

    @Autowired
    private ProcessMapper processMapper;

    public ServerResponse getComInfo(String roomId, int pageNum, int pageSize){
        if (StringUtils.isNotBlank(roomId)){
            int rowCount = informationMapper.checkRoomId(roomId);
            if (rowCount == 0){
                return ServerResponse.createBySuccess("没有当前机房");
            }
        }
        List<Information> informationList = informationMapper.getComInfo(StringUtils.isBlank(roomId)?null:roomId);
        List<ComInfoVo> comInfoList = new ArrayList<>();
        for (Information information:informationList){
            ComInfoVo comInfo = new ComInfoVo();
            comInfo.setMac(information.getMac());
            comInfo.setIp(information.getIp());
            comInfo.setIsBoot(information.getStatus());
            comInfo.setRoomId(information.getRoomId());
            List processList = processMapper.getProcess(information.getMac());
            comInfo.setProcessList(processList);
            comInfoList.add(comInfo);
        }
        PageHelper.startPage(pageNum, pageSize);
        PageInfo pageInfo = new PageInfo(comInfoList);
        return ServerResponse.createBySuccess("comInfoList", pageInfo);
    }
}
