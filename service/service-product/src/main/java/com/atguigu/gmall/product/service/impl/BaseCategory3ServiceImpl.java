package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.product.BaseCategory3;
import com.atguigu.gmall.model.to.CategoryViewTo;
import com.atguigu.gmall.product.mapper.BaseCategory3Mapper;
import com.atguigu.gmall.product.service.BaseCategory3Service;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author ycy
* @description 针对表【base_category3(三级分类表)】的数据库操作Service实现
* @createDate 2022-08-22 19:34:13
*/
@Service
public class BaseCategory3ServiceImpl extends ServiceImpl<BaseCategory3Mapper, BaseCategory3>
    implements BaseCategory3Service{

    @Autowired
    BaseCategory3Mapper baseCategory3Mapper;


    @Override
    public List<BaseCategory3> getCategory2Child(Long c2id) {
        return baseMapper.selectList(new LambdaQueryWrapper<BaseCategory3>().eq(BaseCategory3::getCategory2Id,c2id));
    }

    @Override
    public CategoryViewTo getCategoryView(Long c3Id) {
        CategoryViewTo categoryViewTo = baseCategory3Mapper.getCategoryView(c3Id);
        return categoryViewTo;
    }
}




