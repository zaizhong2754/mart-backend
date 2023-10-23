package com.mart.mapper;

import com.github.pagehelper.Page;
import com.mart.annotation.AutoFill;
import com.mart.dto.GoodsPageQueryDTO;
import com.mart.entity.Goods;
import com.mart.enumeration.OperationType;
import com.mart.vo.GoodsVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface GoodsMapper {
    /**
     * 根据分类id查询商品数量
     * @param categoryId
     * @return
     */
    @Select("select count(id) from goods where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    /**
     * 插入商品数据
     *
     * @param goods
     */
    @AutoFill(value = OperationType.INSERT)
    void insert(Goods goods);

    /**
     * 商品分页查询
     *
     * @param goodsPageQueryDTO
     * @return
     */
    Page<GoodsVO> pageQuery(GoodsPageQueryDTO goodsPageQueryDTO);

    /**
     * 根据主键查询商品
     *
     * @param id
     * @return
     */
    @Select("select * from goods where id = #{id}")
    Goods getById(Long id);

    /**
     * 根据主键删除商品数据
     *
     * @param id
     */
    @Delete("delete from goods where id = #{id}")
    void deleteById(Long id);

    /**
     * 根据商品id集合批量删除商品
     * @param ids
     */
    void deleteByIds(List<Long> ids);

    /**
     * 根据id动态修改商品数据
     *
     * @param goods
     */
    @AutoFill(value = OperationType.UPDATE)
    void update(Goods goods);

    /**
     * 动态条件查询商品
     * @param goods
     * @return
     */
    List<Goods> list(Goods goods);

    /**
     * 根据套餐id查询商品
     * @param setmealId
     * @return
     */
    @Select("select a.* from goods a left join setmeal_goods b on a.id = b.goods_id where b.setmeal_id = #{setmealId}")
    List<Goods> getBySetmealId(Long setmealId);
}
