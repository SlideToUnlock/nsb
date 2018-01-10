package com.nsb.service;

import com.nsb.commons.ServerResponse;

/**
 * @Author:langxy
 * @date 创建时间：2018/1/8 23:36
 */
public interface IManageComputerService {

    ServerResponse distribution(String macs, String roomId);

}
