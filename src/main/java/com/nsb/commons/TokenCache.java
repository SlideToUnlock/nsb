package com.nsb.commons;

import ch.qos.logback.classic.Logger;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @Author:langxy
 * @date 创建时间：2017/11/21 14:33
 */
public class TokenCache {
    private static Logger logger = (Logger) LoggerFactory.getLogger(TokenCache.class);

    public static String TOKEN_PREFIX = "token_";

    private static LoadingCache<String, String> localCache = CacheBuilder.newBuilder().initialCapacity(1000).maximumSize(10000).expireAfterAccess(12, TimeUnit.HOURS)
            .build(new CacheLoader<String, String>() {
                //默认的数据加载实现。当get方法没有值时，就掉用这个方法
                @Override
                public String load(String s) throws Exception {
                    return "null";
                }
            });
    public static void setKey(String key, String value){
        localCache.put(key, value);
    }

    public static String getKey(String key){
        String value = null;
        try {
            value = localCache.get(key);
            if ("null".equals(value)){
                return null;
            }
            return value;
        }catch (Exception e){
            logger.error("localCache get error", e);
        }
        return null;
    }
}
