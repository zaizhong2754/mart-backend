package com.mart.mapper;

import com.mart.entity.SetmealGoods;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealGoodsMapper {
    /**
     * 根据商品id查询对应的套餐id
     *
     * @param goodsIds
     * @return
     */
    //select setmeal_id from setmeal_goods where goods_id in (1,2,3,4)
    List<Long> getSetmealIdsByGoodsIds(List<Long> goodsIds);

    /**
     * 批量保存套餐和商品的关联关系
     * @param setmealGoodses
     */
    void insertBatch(List<SetmealGoods> setmealGoodses);

    /**
     * 根据套餐id删除套餐和商品的关联关系
     * @param setmealId
     */
    @Delete("delete from setmeal_goods where setmeal_id = #{setmealId}")
    void deleteBySetmealId(Long setmealId);

    /**
     * 根据套餐id查询套餐和商品的关联关系
     * @param setmealId
     * @return
     */
    @Select("select * from setmeal_goods where setmeal_id = #{setmealId}")
    List<SetmealGoods> getBySetmealId(Long setmealId);
}
