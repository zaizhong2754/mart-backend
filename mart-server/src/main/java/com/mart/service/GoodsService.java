package com.mart.service;

import com.mart.dto.GoodsDTO;
import com.mart.dto.GoodsPageQueryDTO;
import com.mart.entity.Goods;
import com.mart.result.PageResult;
import com.mart.vo.GoodsVO;

import java.util.List;

public interface GoodsService {

    /**
     * 新增商品和对应的口味
     *
     * @param goodsDTO
     */
    void saveWithFlavor(GoodsDTO goodsDTO);

    /**
     * 商品分页查询
     *
     * @param goodsPageQueryDTO
     * @return
     */
    PageResult pageQuery(GoodsPageQueryDTO goodsPageQueryDTO);

    /**
     * 商品批量删除
     *
     * @param ids
     */
    void deleteBatch(List<Long> ids);

    /**
     * 根据id查询商品和对应的口味数据
     *
     * @param id
     * @return
     */
    GoodsVO getByIdWithFlavor(Long id);

    /**
     * 根据id修改商品基本信息和对应的口味信息
     *
     * @param goodsDTO
     */
    void updateWithFlavor(GoodsDTO goodsDTO);

    /**
     * 商品起售、停售
     * @param status
     * @param id
     */
    void startOrStop(Integer status, Long id);

    /**
     * 根据分类id查询商品
     * @param categoryId
     * @return
     */
    List<Goods> list(Long categoryId);
}
