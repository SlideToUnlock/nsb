package com.nsb.controller.backend;

import com.nsb.commons.Const;
import com.nsb.commons.ResponseCode;
import com.nsb.commons.ServerResponse;
import com.nsb.pojo.User;
import com.nsb.service.IManageComputerService;
import com.nsb.vo.MacVo;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

/**
 * @Author:langxy
 * @date 创建时间：2018/1/5 11:02
 */
@RestController
@RequestMapping("/manage_com")
public class ManageComputerController {

    @Autowired
    private IManageComputerService iManageComputerService;

    @GetMapping("/distribution")
    public ServerResponse distribution(HttpSession session, String macs, String roomId){
        User userItem = (User) session.getAttribute(Const.USER);
        if (userItem == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        if (macs == null || roomId == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ARGUMENT_ERROR.getCode(),ResponseCode.ARGUMENT_ERROR.getDesc());
        }
        return iManageComputerService.distribution(macs, roomId);
    }

}
