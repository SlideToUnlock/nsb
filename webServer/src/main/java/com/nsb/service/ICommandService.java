package com.nsb.service;

import com.nsb.commons.ServerResponse;

/**
 * @Author:langxy
 * @date 创建时间：2018/1/8 14:52
 */
public interface ICommandService {

    ServerResponse activeCommandShutdown(String mac, String roomId, int pageNum, int pageSize);

    ServerResponse activeCommandGetProcess(String mac, String roomId, int pageNum, int pageSize);

}
