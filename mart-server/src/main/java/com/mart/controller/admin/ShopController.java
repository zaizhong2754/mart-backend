package com.mart.controller.admin;

import com.mart.constant.StatusConstant;
import com.mart.result.Result;
import com.mart.service.ShopService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javafx.animation.Animation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

@RestController("adminShopController")
@RequestMapping("/admin/shop")
@Api(tags = "店铺相关接口")
@Slf4j
public class ShopController {


    @Autowired
    private ShopService shopService;

    /**
     * 获取店铺的营业状态
     * @return
     */
    @GetMapping("/status")
    @ApiOperation("获取店铺的营业状态")
    public Result<Integer> getStatus(){
        Integer status = shopService.getStatus("status");
        return Result.success(status);
    }

    /**
     * 设置店铺的营业状态
     * @param status
     * @return
     */
    @PutMapping("/{status}")
    @ApiOperation("设置店铺的营业状态")
    public Result setStatus(@PathVariable Integer status){
        Integer s = shopService.setStatus("status", status);
        log.info("设置店铺的营业状态为：{}",s == StatusConstant.ENABLE ? "营业中" : "打烊中");
        return Result.success();
    }
}
