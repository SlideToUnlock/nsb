package com.nsb.listener;

import com.nsb.netty.server.InitNetty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @Author:langxy
 * @date 创建时间：2018/1/4 20:13
 */
@Component("springListener")
public class SpringListener implements ApplicationListener<ContextRefreshedEvent>{

    private Logger logger = LoggerFactory.getLogger(SpringListener.class);

    @Autowired
    private InitNetty initNetty;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent){
        if (contextRefreshedEvent.getApplicationContext().getParent() == null){
            logger.info("spring boot加载完成");
            new Thread(() -> initNetty.start()).start();
        }
    }
}
