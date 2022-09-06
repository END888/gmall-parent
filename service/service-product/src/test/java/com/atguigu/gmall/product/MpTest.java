package com.atguigu.gmall.product;

import com.atguigu.gmall.model.product.BaseCategory3;
import com.atguigu.gmall.product.mapper.BaseCategory3Mapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class MpTest {
    @Autowired
    BaseCategory3Mapper baseCategory3Mapper;

    @Test
    public void test01(){
        List<Object> objects = baseCategory3Mapper.selectObjs(new LambdaQueryWrapper<BaseCategory3>().select(BaseCategory3::getName));
        objects.forEach(System.out::println);
    }
}
