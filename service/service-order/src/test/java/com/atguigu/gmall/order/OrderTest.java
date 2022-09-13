package com.atguigu.gmall.order;

import com.atguigu.gmall.model.order.OrderInfo;
import com.atguigu.gmall.order.mapper.OrderInfoMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest
public class OrderTest {


    @Autowired
    OrderInfoMapper orderInfoMapper;

    @Test
    void test01(){
        OrderInfo orderInfo = orderInfoMapper.selectById(205L);
        System.out.println("orderInfo = " + orderInfo);
    }

    @Test
    void test02(){
        OrderInfo info = new OrderInfo();
        info.setTotalAmount(new BigDecimal("777"));
        info.setUserId(1L);
        orderInfoMapper.insert(info);

        System.out.println("1号用户订单插入完成...去 1 库 1 表找");

        OrderInfo info2 = new OrderInfo();
        info2.setTotalAmount(new BigDecimal("666"));
        info2.setUserId(2L);
        orderInfoMapper.insert(info2);
        System.out.println("2 号用户订单插入完成...去 0 库 2 表找");
    }

    @Test
    void test03(){
        QueryWrapper<OrderInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",2L);
        List<OrderInfo> infos = orderInfoMapper.selectList(wrapper);
        for (OrderInfo info : infos) {
            System.out.println(info);
        }
    }
}
