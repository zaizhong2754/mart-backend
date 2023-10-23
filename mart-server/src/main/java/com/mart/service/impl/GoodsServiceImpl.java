package com.mart.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mart.constant.MessageConstant;
import com.mart.constant.StatusConstant;
import com.mart.dto.GoodsDTO;
import com.mart.dto.GoodsPageQueryDTO;
import com.mart.entity.Goods;
import com.mart.entity.GoodsFlavor;
import com.mart.exception.DeletionNotAllowedException;
import com.mart.mapper.GoodsFlavorMapper;
import com.mart.mapper.GoodsMapper;
import com.mart.mapper.SetmealGoodsMapper;
import com.mart.result.PageResult;
import com.mart.service.GoodsService;
import com.mart.vo.GoodsVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private GoodsFlavorMapper goodsFlavorMapper;
    @Autowired
    private SetmealGoodsMapper setmealGoodsMapper;

    /**
     * 新增商品和对应的口味
     *
     * @param goodsDTO
     */
    @Transactional
    public void saveWithFlavor(GoodsDTO goodsDTO) {

        Goods goods = new Goods();
        BeanUtils.copyProperties(goodsDTO, goods);

        //向商品表插入1条数据
        goodsMapper.insert(goods);//后绪步骤实现

        //获取insert语句生成的主键值
        Long goodsId = goods.getId();

        List<GoodsFlavor> flavors = goodsDTO.getFlavors();
        if (flavors != null && flavors.size() > 0) {
            flavors.forEach(goodsFlavor -> {
                goodsFlavor.setGoodsId(goodsId);
            });
            //向口味表插入n条数据
            goodsFlavorMapper.insertBatch(flavors);//后绪步骤实现
        }
    }

    /**
     * 商品分页查询
     *
     * @param goodsPageQueryDTO
     * @return
     */
    public PageResult pageQuery(GoodsPageQueryDTO goodsPageQueryDTO) {
        PageHelper.startPage(goodsPageQueryDTO.getPage(), goodsPageQueryDTO.getPageSize());
        Page<GoodsVO> page = goodsMapper.pageQuery(goodsPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 商品批量删除
     *
     * @param ids
     */
    @Transactional
    public void deleteBatch(List<Long> ids) {
        //判断当前商品是否能够删除---是否存在起售中的商品
        for (Long id : ids) {
            Goods goods = goodsMapper.getById(id);
            if (goods.getStatus() == StatusConstant.ENABLE) {
                //当前商品处于起售中，不能删除
                throw new DeletionNotAllowedException(MessageConstant.GOODS_ON_SALE);
            }
        }

        //判断当前商品是否能够删除---是否被套餐关联了
        List<Long> setmealIds = setmealGoodsMapper.getSetmealIdsByGoodsIds(ids);
        if (setmealIds != null && setmealIds.size() > 0) {
            //当前商品被套餐关联了，不能删除
            throw new DeletionNotAllowedException(MessageConstant.GOODS_BE_RELATED_BY_SETMEAL);
        }

        //删除商品表中的商品数据
//        for (Long id : ids) {
//            goodsMapper.deleteById(id);
//            //删除商品关联的口味数据
//            goodsFlavorMapper.deleteByGoodsId(id);
//        }

        //根据商品id集合批量删除商品数据
        //sql: delete from goods where id in (?,?,?)
        goodsMapper.deleteByIds(ids);

        //根据商品id集合批量删除关联的口味数据
        //sql: delete from goods_flavor where goods_id in (?,?,?)
        goodsFlavorMapper.deleteByGoodsIds(ids);
    }

    /**
     * 根据id查询商品和对应的口味数据
     *
     * @param id
     * @return
     */
    @Transactional
    public GoodsVO getByIdWithFlavor(Long id) {
        //根据id查询商品数据
        Goods goods = goodsMapper.getById(id);

        //根据商品id查询口味数据
        List<GoodsFlavor> goodsFlavors = goodsFlavorMapper.getByGoodsId(id);

        //将查询到的数据封装到VO
        GoodsVO goodsVO = new GoodsVO();
        BeanUtils.copyProperties(goods, goodsVO);
        goodsVO.setFlavors(goodsFlavors);

        return goodsVO;
    }

    /**
     * 根据id修改商品基本信息和对应的口味信息
     *
     * @param goodsDTO
     */
    public void updateWithFlavor(GoodsDTO goodsDTO) {
        Goods goods = new Goods();
        BeanUtils.copyProperties(goodsDTO, goods);

        //修改商品表基本信息
        goodsMapper.update(goods);

        //删除原有的口味数据
        goodsFlavorMapper.deleteByGoodsId(goodsDTO.getId());

        //重新插入口味数据
        List<GoodsFlavor> flavors = goodsDTO.getFlavors();
        if (flavors != null && flavors.size() > 0) {
            flavors.forEach(goodsFlavor -> {
                goodsFlavor.setGoodsId(goodsDTO.getId());
            });
            //向口味表插入n条数据
            goodsFlavorMapper.insertBatch(flavors);
        }
    }

    /**
     * 商品起售、停售
     * @param status
     * @param id
     */
    public void startOrStop(Integer status, Long id) {
        Goods goods = new Goods();
        goods.setId(id);
        goods.setStatus(status);

        //修改商品表基本信息
        goodsMapper.update(goods);
    }

    /**
     * 根据分类id查询商品
     * @param categoryId
     * @return
     */
    public List<Goods> list(Long categoryId) {
        Goods goods = Goods.builder()
                .categoryId(categoryId)
                .status(StatusConstant.ENABLE)
                .build();
        return goodsMapper.list(goods);
    }
}
