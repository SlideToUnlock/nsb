package com.nsb.service;

import com.nsb.commons.ServerResponse;
import com.nsb.pojo.User;

import javax.servlet.http.HttpSession;

/**
 * @Author:langxy
 * @date 创建时间：2018/1/9 10:39
 */
public interface IUserService {

    ServerResponse login(String username, String password, HttpSession session);

    ServerResponse resetPassword(String username);

    ServerResponse updatePassword(String username, String passwordOld, String passwordNew);

    ServerResponse addUser(User user);

    ServerResponse delUser(String username);

    ServerResponse getUsers();

}
