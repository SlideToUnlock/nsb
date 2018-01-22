package com.nsb.controller.protal;

import com.nsb.commons.Const;
import com.nsb.commons.ResponseCode;
import com.nsb.commons.ServerResponse;
import com.nsb.dao.UserMapper;
import com.nsb.pojo.User;
import com.nsb.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * @Author:langxy
 * @date 创建时间：2018/1/9 10:35
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService iUserService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ServerResponse login(HttpSession session, String username, String password){
        ServerResponse<User> response = iUserService.login(username, password);
        if (response.isSuccess()){
            session.setAttribute(Const.USER, response.getData());
        }
        return response;
    }

    @RequestMapping("/logout")
    public ServerResponse logout(HttpSession session){
         session.removeAttribute(Const.USER);
         return ServerResponse.createBySuccessMessage("退出成功");
    }

    @RequestMapping(value = "/forget_password", method = RequestMethod.POST)
    public ServerResponse forgetPassword(HttpSession session, String username, String answer,String passwordNew){
        User user = (User)session.getAttribute(Const.USER);
        if (user != null){
            return ServerResponse.createByErrorMessage("请退出当前登录");
        }
        if (username == null || answer == null || passwordNew == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ARGUMENT_ERROR.getCode(),ResponseCode.ARGUMENT_ERROR.getDesc());
        }
        return iUserService.forgetPassword(username, answer, passwordNew);
    }

    @RequestMapping(value = "/update_password", method = RequestMethod.POST)
    public ServerResponse updatePassword(HttpSession session, String passwordOld, String passwordNew){
        User user = (User) session.getAttribute(Const.USER);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iUserService.updatePassword(user.getUsername(),passwordOld,passwordNew);
    }

    @RequestMapping(value = "/add_user", method = RequestMethod.POST)
    public ServerResponse addUser(HttpSession session, User user){
        User userItem = (User) session.getAttribute(Const.USER);
        if (userItem == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        if (userItem.getRole() == Const.Role.ROLE_CUSTOMER){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_PERMISSION.getCode(), ResponseCode.NEED_PERMISSION.getDesc());
        }
        return iUserService.addUser(user);
    }

    @RequestMapping(value = "/del_user", method = RequestMethod.POST)
    public ServerResponse delUser(HttpSession session, String username){
        User userItem = (User) session.getAttribute(Const.USER);
        if (userItem.getUsername() == username){
            return ServerResponse.createBySuccessMessage("无法删除自己");
        }
        if (userItem == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        if (userItem.getRole() == Const.Role.ROLE_CUSTOMER){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_PERMISSION.getCode(), ResponseCode.NEED_PERMISSION.getDesc());
        }
        return iUserService.delUser(username);
    }
    @RequestMapping(value = "/get_users", method = RequestMethod.POST)
    public ServerResponse getUsers(HttpSession session){
        User user = (User)session.getAttribute(Const.USER);
        if (user == null){
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        if (user.getRole() == Const.Role.ROLE_CUSTOMER){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_PERMISSION.getCode(), ResponseCode.NEED_PERMISSION.getDesc());
        }
        return iUserService.getUsers();
    }
}
