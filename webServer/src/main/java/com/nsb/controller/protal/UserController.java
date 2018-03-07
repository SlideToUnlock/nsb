package com.nsb.controller.protal;

import com.nsb.commons.Const;
import com.nsb.commons.ResponseCode;
import com.nsb.commons.ServerResponse;
import com.nsb.dao.UserMapper;
import com.nsb.pojo.User;
import com.nsb.service.IUserService;
import com.nsb.util.JwtUtils;
import com.nsb.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private UserMapper userMapper;

    @RequestMapping(value = "/login")
    public ServerResponse login(String username, String password, HttpSession session) {
        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username, MD5Util.MD5EncodeUtf8(password));
        // Perform the security
        final Authentication authentication = authenticationManager.authenticate(upToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        final String token = new JwtUtils().generateToken(userDetails);
        return ServerResponse.createBySuccessToken("登陆成功", token);
    }

    @RequestMapping("/logout")
    public ServerResponse logout(HttpSession session) {
        session.removeAttribute(Const.USER);
        return ServerResponse.createBySuccessMessage("退出成功");
    }

//    @RequestMapping(value = "/forget_password", method = RequestMethod.POST)
//    public ServerResponse forgetPassword(HttpSession session, String username, String answer, String passwordNew) {
//        User user = (User) session.getAttribute(Const.USER);
//        if (user != null) {
//            return ServerResponse.createByErrorMessage("请退出当前登录");
//        }
//        if (username == null || answer == null || passwordNew == null) {
//            return ServerResponse.createByErrorCodeMessage(ResponseCode.ARGUMENT_ERROR.getCode(), ResponseCode.ARGUMENT_ERROR.getDesc());
//        }
//        return iUserService.forgetPassword(username, answer, passwordNew);
//    }

    @RequestMapping(value = "/reset_password")
    public ServerResponse forgetPassword(HttpServletRequest request, String username) {
        String userItem = new JwtUtils().getUsernameFromToken(request);
        User user = userMapper.findByUsername(userItem);
        if (user.getRole().equals(Const.Role.ROLE_CUSTOMER)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_PERMISSION.getCode(), ResponseCode.NEED_PERMISSION.getDesc());
        }
        return iUserService.resetPassword(username);
    }

    @RequestMapping(value = "/update_password")
    public ServerResponse updatePassword(HttpSession session, String passwordOld, String passwordNew) {
        User user = (User) session.getAttribute(Const.USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iUserService.updatePassword(user.getUsername(), passwordOld, passwordNew);
    }


    //    @RequestMapping(value = "/add_user")
    @PostMapping(value = "/addUser")
    public ServerResponse addUser(@RequestBody User user,HttpServletRequest request) {
        // 当前登录用户
        String users = new JwtUtils().getUsernameFromToken(request);
        User userItem = userMapper.findByUsername(users);
        if (userItem == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        if (userItem.getRole() == Const.Role.ROLE_CUSTOMER) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_PERMISSION.getCode(), ResponseCode.NEED_PERMISSION.getDesc());
        }
        return iUserService.addUser(user);
    }

    @RequestMapping(value = "/del_user")
    public ServerResponse delUser(HttpServletRequest request, String username) {
        // 当前登录用户
        String user = new JwtUtils().getUsernameFromToken(request);

        User userItem = userMapper.findByUsername(user);
        if (user == username) {
            return ServerResponse.createBySuccessMessage("无法删除自己");
        }
        if (userItem == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        if (userItem.getRole() == Const.Role.ROLE_CUSTOMER) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_PERMISSION.getCode(), ResponseCode.NEED_PERMISSION.getDesc());
        }
        return iUserService.delUser(username);
    }

    @RequestMapping(value = "/get_users")
    public ServerResponse getUsers(HttpServletRequest request) {
        String user = new JwtUtils().getUsernameFromToken(request);
//        if (user == null) {
//            return ServerResponse.createByErrorMessage("用户未登录");
//        }
//        if (user.getRole() == Const.Role.ROLE_CUSTOMER) {
//            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_PERMISSION.getCode(), ResponseCode.NEED_PERMISSION.getDesc());
//        }
        return iUserService.getUsers();
    }
}
