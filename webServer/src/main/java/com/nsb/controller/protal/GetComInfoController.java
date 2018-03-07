package com.nsb.controller.protal;

import com.nsb.commons.Const;
import com.nsb.commons.ResponseCode;
import com.nsb.commons.ServerResponse;
import com.nsb.dao.UserMapper;
import com.nsb.pojo.User;
import com.nsb.service.IGetComInfoService;
import com.nsb.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
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
    @Autowired
    private UserMapper userMapper;

    @RequestMapping("/get_info")
    public ServerResponse getComInfo(HttpServletRequest request, @RequestParam(value = "roomId", defaultValue = "") String roomId, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        // 判断用户登录
        // 当前登录用户
        String user = new JwtUtils().getUsernameFromToken(request);

        User userItem = userMapper.findByUsername(user);

        if (userItem == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
            return iGetComInfoService.getComInfo(roomId, pageNum, pageSize);
        }
    }