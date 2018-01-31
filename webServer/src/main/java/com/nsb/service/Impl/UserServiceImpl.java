package com.nsb.service.Impl;

import com.nsb.commons.Const;
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
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

/**
 * @Author:langxy
 * @date 创建时间：2018/1/9 10:39
 */
@Service("iUserService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsService userDetailsService;

    public ServerResponse login(String username, String password, HttpSession session){
        String md5Password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.checkLogin(username, md5Password);
        if (user != null){
            session.setAttribute(Const.USER, user);
            UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username, MD5Util.MD5EncodeUtf8(password));
            // Perform the security
            final Authentication authentication = authenticationManager.authenticate(upToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            final String token = new JwtUtils().generateToken(userDetails);
            return ServerResponse.createBySuccessToken("登陆成功",token);
        }
        return ServerResponse.createByErrorMessage("用户名或者密码错误");
    }

    public ServerResponse resetPassword(String username){
        String md5Password = MD5Util.MD5EncodeUtf8("123456");
        int rowCount = userMapper.resetPassword(username, md5Password);
        if (rowCount > 0){
            return ServerResponse.createBySuccessMessage("重置密码成功");
        }
        return ServerResponse.createByErrorMessage("重置密码失败");
    }

    public ServerResponse updatePassword(String username, String passwordOld, String passwordNew){
        String md5PasswordOld = MD5Util.MD5EncodeUtf8(passwordOld);
        String md5PasswordNew = MD5Util.MD5EncodeUtf8(passwordNew);
        int rowCount = userMapper.updatePassword(username, md5PasswordOld, md5PasswordNew);
        if (rowCount > 0){
            return ServerResponse.createBySuccessMessage("修改密码成功");
        }
        return ServerResponse.createByErrorMessage("修改密码失败");
    }

    public ServerResponse addUser(User user){
        int rowCount = userMapper.checkUsername(user.getUsername());
        if (rowCount == 0){
            user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
            userMapper.insert(user);
            return ServerResponse.createBySuccessMessage("添加用户成功");
        }
        return ServerResponse.createByErrorMessage("当前用户名已存在");
    }


    public ServerResponse delUser(String username){

        int rowCount = userMapper.checkUsername(username);
        if (rowCount > 0){
            userMapper.deleteUser(username);
            return ServerResponse.createBySuccessMessage("删除用户成功");
        }
        return ServerResponse.createByErrorMessage("删除用户失败");
    }

    public ServerResponse getUsers(){
        return ServerResponse.createBySuccess("获取用户列表成功", userMapper.getUsers());
    }
}
