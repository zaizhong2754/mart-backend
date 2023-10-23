package com.mart.service;

public interface ShopService {

    /**
     * 获取店铺的营业状态
     * @return
     */
    Integer getStatus(String cacheKey);

    /**
     * 设置店铺的营业状态
     * @param status
     * @return
     */
    Integer setStatus(String cacheKey, Integer status);
}
