package com.mart.service.impl;

import com.mart.service.ShopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ShopServiceImpl implements ShopService {

    /**
     * 获取店铺的营业状态
     * @return
     */
    @Cacheable(cacheNames = "shopCache", key = "#cacheKey") // key: shopCache::status
    public Integer getStatus(String cacheKey) {
        return 0;
    }

    /**
     * 设置店铺的营业状态
     * @param status
     * @return
     */
    @CachePut(cacheNames = "shopCache", key = "#cacheKey") // key: shopCache::status
    public Integer setStatus(String cacheKey, Integer status) {
        return status;
    }
}
