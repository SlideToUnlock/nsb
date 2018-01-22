package com.nsb.controller.protal;

import com.nsb.commons.Const;
import com.nsb.commons.ResponseCode;
import com.nsb.commons.ServerResponse;
import com.nsb.pojo.User;
import com.nsb.service.IGetComInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * @Author:langxy
 * @date 创建时间：2018/1/5 11:08
 */
@RestController
@RequestMapping("/get_com_info")
public class GetComInfoController {

    @Autowired
    private IGetComInfoService iGetComInfoService;

    @RequestMapping("/get_info")
    public ServerResponse getComInfo(HttpSession session, @RequestParam(value = "roomId", defaultValue = "") String roomId, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
        User user = (User)session.getAttribute(Const.USER);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iGetComInfoService.getComInfo(roomId, pageNum, pageSize);
    }
}