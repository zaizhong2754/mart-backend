package com.mart.controller.admin;


import com.mart.dto.GoodsDTO;
import com.mart.dto.GoodsPageQueryDTO;
import com.mart.entity.Goods;
import com.mart.result.PageResult;
import com.mart.result.Result;
import com.mart.service.GoodsService;
import com.mart.vo.GoodsVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品管理
 */
@RestController
@RequestMapping("/admin/goods")
@Api(tags = "商品相关接口")
@Slf4j
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    /**
     * 新增商品
     *
     * @param goodsDTO
     * @return
     */
    @PostMapping
    @ApiOperation("新增商品")
    public Result save(@RequestBody GoodsDTO goodsDTO) {
        log.info("新增商品：{}", goodsDTO);
        goodsService.saveWithFlavor(goodsDTO);//后绪步骤开发
        return Result.success();
    }

    /**
     * 商品分页查询
     *
     * @param goodsPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("商品分页查询")
    public Result<PageResult> page(GoodsPageQueryDTO goodsPageQueryDTO) {
        log.info("商品分页查询: {}", goodsPageQueryDTO);
        PageResult pageResult = goodsService.pageQuery(goodsPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 商品批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    @ApiOperation("商品批量删除")
    public Result delete(@RequestParam List<Long> ids) {
        log.info("商品批量删除: {}", ids);
        goodsService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 根据id查询商品
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询商品")
    public Result<GoodsVO> getById(@PathVariable Long id) {
        log.info("根据id查询商品: {}", id);
        GoodsVO goodsVO = goodsService.getByIdWithFlavor(id);
        return Result.success(goodsVO);
    }

    /**
     * 修改商品
     *
     * @param goodsDTO
     * @return
     */
    @PutMapping
    @ApiOperation("修改商品")
    public Result update(@RequestBody GoodsDTO goodsDTO) {
        log.info("修改商品: {}", goodsDTO);
        goodsService.updateWithFlavor(goodsDTO);
        return Result.success();
    }

    /**
     * 商品起售停售
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("商品起售停售")
    public Result startOrStop(@PathVariable Integer status, Long id) {
        goodsService.startOrStop(status, id);
        return Result.success();
    }

    /**
     * 根据分类id查询商品
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询商品")
    public Result<List<Goods>> list(Long categoryId){
        List<Goods> list = goodsService.list(categoryId);
        return Result.success(list);
    }
}
