package com.nsb.controller.protal;

import com.nsb.commons.Const;
import com.nsb.commons.ResponseCode;
import com.nsb.commons.ServerResponse;
import com.nsb.dao.UserMapper;
import com.nsb.pojo.User;
import com.nsb.service.ICommandService;
import com.nsb.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @Author:langxy
 * @date 创建时间：2018/1/5 10:56
 */
@RestController
@RequestMapping("/command")
public class CommandController {

    @Autowired
    private ICommandService iCommandService;
    @Autowired
    private UserMapper userMapper;

    @RequestMapping("/shutdown")
    public ServerResponse shutdown(HttpServletRequest request, @RequestParam(value = "mac", defaultValue = "") String mac, @RequestParam(value = "roomId", defaultValue = "") String roomId, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
        String user = new JwtUtils().getUsernameFromToken(request);

        User userItem = userMapper.findByUsername(user);
        if (userItem == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCommandService.activeCommandShutdown(mac, roomId, pageNum, pageSize);
    }

    @RequestMapping("/get_process")
    public ServerResponse getProcess(HttpServletRequest request, @RequestParam(value = "mac", defaultValue = "") String mac, @RequestParam(value = "roomId", defaultValue = "") String roomId, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
        String user = new JwtUtils().getUsernameFromToken(request);

        User userItem = userMapper.findByUsername(user);
        if (userItem == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCommandService.activeCommandGetProcess(mac, roomId, pageNum, pageSize);
    }
}
