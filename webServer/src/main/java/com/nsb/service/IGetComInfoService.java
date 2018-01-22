package com.nsb.service;

import com.nsb.commons.ServerResponse;

/**
 * @Author:langxy
 * @date 创建时间：2018/1/8 9:39
 */
public interface IGetComInfoService {

    ServerResponse getComInfo(String roomId, int pageNum, int pageSize);

}
