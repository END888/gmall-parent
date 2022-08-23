package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.product.BaseAttrInfo;
import com.atguigu.gmall.model.product.BaseAttrValue;
import com.atguigu.gmall.product.mapper.BaseAttrInfoMapper;
import com.atguigu.gmall.product.mapper.BaseAttrValueMapper;
import com.atguigu.gmall.product.service.BaseAttrInfoService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* @author ycy
* @description 针对表【base_attr_info(属性表)】的数据库操作Service实现
* @createDate 2022-08-22 21:02:12
*/
@Service
public class BaseAttrInfoServiceImpl extends ServiceImpl<BaseAttrInfoMapper, BaseAttrInfo>
    implements BaseAttrInfoService{

    @Autowired
    BaseAttrInfoMapper baseAttrInfoMapper;

    @Autowired
    BaseAttrValueMapper baseAttrValueMapper;

    @Override
    public List<BaseAttrInfo> getBaseAttrInfoByCategoryId(Long category1Id, Long category2Id, Long category3Id) {
        // 查询指定分类下的所有属性名和值
        return baseMapper.getBaseAttrInfoByCategoryId(category1Id,category2Id,category3Id);
    }

    /**
     * 保存、修改属性信息二合一的方法
     * @param baseAttrInfo
     */
    @Override
    public void saveAttrInfo(BaseAttrInfo baseAttrInfo) {
        if (baseAttrInfo.getId() == null){
            // 1、进行属性新增操作
            addBaseAttrInfo(baseAttrInfo);
        }else {
            // 2、进行属性修改操作
            updateBaseAttrInfo(baseAttrInfo);
        }
    }

    @Override
    public List<BaseAttrValue> getBaseAttrValueByAttrId(Long attrId) {
        return baseAttrValueMapper.selectList(new LambdaQueryWrapper<BaseAttrValue>().eq(BaseAttrValue::getAttrId,attrId));
    }

    private void updateBaseAttrInfo(BaseAttrInfo baseAttrInfo) {
        // 1、修改属性名
        baseAttrInfoMapper.updateById(baseAttrInfo);

        // 2、修改属性值
        List<BaseAttrValue> attrValueList = baseAttrInfo.getAttrValueList();
        // 前端提交来的所有属性值id
        ArrayList<Long> ids = new ArrayList<>();
        for (BaseAttrValue attrValue : attrValueList) {
            Long id = attrValue.getId();
            if (id != null){
                ids.add(id);
            }
        }

        if (ids.size() > 0){
            // 说明包含之前存在的，进行部分删除
            baseAttrValueMapper.delete(new LambdaQueryWrapper<BaseAttrValue>()
                    .eq(BaseAttrValue::getAttrId,baseAttrInfo.getId())
                    .notIn(BaseAttrValue::getId,ids));
        }else {
            // 全部删除
            baseAttrValueMapper.delete(new LambdaQueryWrapper<BaseAttrValue>().eq(BaseAttrValue::getAttrId,baseAttrInfo.getId()));
        }

        for (BaseAttrValue attrValue : attrValueList) {
            if (attrValue.getId() != null){
                // 修改
                baseAttrValueMapper.updateById(attrValue);
            }else {
                // 新增
                attrValue.setAttrId(baseAttrInfo.getId());
                baseAttrValueMapper.insert(attrValue);
            }
        }

    }

    private void addBaseAttrInfo(BaseAttrInfo baseAttrInfo) {
        // 1、保存属性名
        baseAttrInfoMapper.insert(baseAttrInfo);
        // 获取到新增后生成的属性名id
        Long id = baseAttrInfo.getId();
        // 2、保存属性值
        List<BaseAttrValue> attrValueList = baseAttrInfo.getAttrValueList();
        for (BaseAttrValue attrValue : attrValueList) {
            // 回填属性名记录的自增id
            attrValue.setAttrId(id);
            baseAttrValueMapper.insert(attrValue);
        }

    }
}




