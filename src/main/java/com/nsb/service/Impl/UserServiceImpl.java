package com.nsb.service.Impl;

import com.nsb.commons.ServerResponse;
import com.nsb.dao.UserMapper;
import com.nsb.pojo.User;
import com.nsb.service.IUserService;
import com.nsb.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author:langxy
 * @date 创建时间：2018/1/9 10:39
 */
@Service("iUserService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;


    public ServerResponse login(String username, String password){
        String md5Password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.checkLogin(username, md5Password);
        if (user != null){
            return ServerResponse.createBySuccess("登录成功",user);
        }
        return ServerResponse.createByErrorMessage("用户名或者密码错误");
    }

    public ServerResponse forgetPassword(String username, String answer , String passwordNew){
        int rowCount = userMapper.checkAnswer(username, answer);
        if (rowCount > 0){
            String md5Password = MD5Util.MD5EncodeUtf8(passwordNew);
            userMapper.resetPassword(username, answer, md5Password);
            return ServerResponse.createBySuccessMessage("重置密码成功");
        }
        return ServerResponse.createByErrorMessage("用户名或者密码输入错误");
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
        return ServerResponse.createByErrorMessage("无当前用户，无法删除");
    }
}
