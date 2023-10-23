package com.mart.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mart.constant.MessageConstant;
import com.mart.constant.StatusConstant;
import com.mart.dto.SetmealDTO;
import com.mart.dto.SetmealPageQueryDTO;
import com.mart.entity.Goods;
import com.mart.entity.Setmeal;
import com.mart.entity.SetmealGoods;
import com.mart.exception.DeletionNotAllowedException;
import com.mart.exception.SetmealEnableFailedException;
import com.mart.mapper.GoodsMapper;
import com.mart.mapper.SetmealGoodsMapper;
import com.mart.mapper.SetmealMapper;
import com.mart.result.PageResult;
import com.mart.service.SetmealService;
import com.mart.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 套餐业务实现
 */
@Service
@Slf4j
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealGoodsMapper setmealGoodsMapper;
    @Autowired
    private GoodsMapper goodsMapper;

    /**
     * 新增套餐，同时需要保存套餐和商品的关联关系
     * @param setmealDTO
     */
    @Transactional
    public void saveWithGoods(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);

        //向套餐表插入数据
        setmealMapper.insert(setmeal);

        //获取生成的套餐id
        Long setmealId = setmeal.getId();

        List<SetmealGoods> setmealGoodses = setmealDTO.getSetmealGoodses();
        setmealGoodses.forEach(setmealGoods -> {
            setmealGoods.setSetmealId(setmealId);
        });

        //保存套餐和商品的关联关系
        setmealGoodsMapper.insertBatch(setmealGoodses);
    }

    /**
     * 分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        int pageNum = setmealPageQueryDTO.getPage();
        int pageSize = setmealPageQueryDTO.getPageSize();

        PageHelper.startPage(pageNum, pageSize);
        Page<SetmealVO> page = setmealMapper.pageQuery(setmealPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 批量删除套餐
     * @param ids
     */
    @Transactional
    public void deleteBatch(List<Long> ids) {
        ids.forEach(id -> {
            Setmeal setmeal = setmealMapper.getById(id);
            if(StatusConstant.ENABLE == setmeal.getStatus()){
                //起售中的套餐不能删除
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        });

        ids.forEach(setmealId -> {
            //删除套餐表中的数据
            setmealMapper.deleteById(setmealId);
            //删除套餐商品关系表中的数据
            setmealGoodsMapper.deleteBySetmealId(setmealId);
        });
    }

    /**
     * 根据id查询套餐和套餐商品关系
     *
     * @param id
     * @return
     */
    public SetmealVO getByIdWithGoods(Long id) {
        Setmeal setmeal = setmealMapper.getById(id);
        List<SetmealGoods> setmealGoodses = setmealGoodsMapper.getBySetmealId(id);

        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmeal, setmealVO);
        setmealVO.setSetmealGoodses(setmealGoodses);

        return setmealVO;
    }

    /**
     * 修改套餐
     *
     * @param setmealDTO
     */
    @Transactional
    public void update(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);

        //1、修改套餐表，执行update
        setmealMapper.update(setmeal);

        //套餐id
        Long setmealId = setmealDTO.getId();

        //2、删除套餐和商品的关联关系，操作setmeal_Goods表，执行delete
        setmealGoodsMapper.deleteBySetmealId(setmealId);

        List<SetmealGoods> setmealGoodses = setmealDTO.getSetmealGoodses();
        setmealGoodses.forEach(setmealGoods -> {
            setmealGoods.setSetmealId(setmealId);
        });
        //3、重新插入套餐和商品的关联关系，操作setmeal_goods表，执行insert
        setmealGoodsMapper.insertBatch(setmealGoodses);
    }

    /**
     * 套餐起售、停售
     * @param status
     * @param id
     */
    public void startOrStop(Integer status, Long id) {
        //起售套餐时，判断套餐内是否有停售商品，有停售商品提示"套餐内包含未启售商品，无法启售"
        if(status == StatusConstant.ENABLE){
            //select a.* from goods a left join setmeal_goods b on a.id = b.goods_id where b.setmeal_id = ?
            List<Goods> goodsList = goodsMapper.getBySetmealId(id);
            if(goodsList != null && goodsList.size() > 0){
                goodsList.forEach(goods -> {
                    if(StatusConstant.DISABLE == goods.getStatus()){
                        throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                    }
                });
            }
        }

        Setmeal setmeal = Setmeal.builder()
                .id(id)
                .status(status)
                .build();
        setmealMapper.update(setmeal);
    }
}
