package com.mart.service;

import com.mart.dto.SetmealDTO;
import com.mart.dto.SetmealPageQueryDTO;
import com.mart.result.PageResult;
import com.mart.vo.SetmealVO;

import java.util.List;

public interface SetmealService {

    /**
     * 新增套餐，同时需要保存套餐和商品的关联关系
     * @param setmealDTO
     */
    void saveWithGoods(SetmealDTO setmealDTO);

    /**
     * 分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 批量删除套餐
     * @param ids
     */
    void deleteBatch(List<Long> ids);

    /**
     * 根据id查询套餐和关联的商品数据
     * @param id
     * @return
     */
    SetmealVO getByIdWithGoods(Long id);

    /**
     * 修改套餐
     * @param setmealDTO
     */
    void update(SetmealDTO setmealDTO);

    /**
     * 套餐起售、停售
     * @param status
     * @param id
     */
    void startOrStop(Integer status, Long id);
}
