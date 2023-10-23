package com.mart.mapper;

import com.mart.entity.GoodsFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface GoodsFlavorMapper {

    /**
     * 批量插入口味数据
     * @param flavors
     */
    void insertBatch(List<GoodsFlavor> flavors);

    /**
     * 根据商品id删除对应的口味数据
     * @param goodsId
     */
    @Delete("delete from goods_flavor where goods_id = #{goodsId}")
    void deleteByGoodsId(Long goodsId);

    /**
     * 根据商品id集合批量删除关联的口味数据
     * @param goodsIds
     */
    void deleteByGoodsIds(List<Long> goodsIds);

    /**
     * 根据商品id查询对应的口味数据
     * @param goodsId
     * @return
     */
    @Select("select * from goods_flavor where goods_id = #{goodsId}")
    List<GoodsFlavor> getByGoodsId(Long goodsId);
}
