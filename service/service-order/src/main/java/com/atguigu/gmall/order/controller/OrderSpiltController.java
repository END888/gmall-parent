package com.atguigu.gmall.order.controller;


import com.atguigu.gmall.model.vo.order.OrderWareMapVo;
import com.atguigu.gmall.model.vo.order.WareChildOrderVo;
import com.atguigu.gmall.order.biz.OrderBizService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 拆单
 */
@Slf4j
@RequestMapping("/api/order")
@RestController
public class OrderSpiltController {


    @Autowired
    OrderBizService orderBizService;
    /**
     * 拆单
     * @return
     */
//    @PostMapping("/orderSplit")
//    public List<WareChildOrderVo> orderSplit(OrderWareMapVo params){
//        log.info("订单执行拆单:{}",params);
//        //把这个大订单，拆分成两个子订单（保存数据库）
//        return orderBizService.orderSplit(params);
//    }

    /**
     * 拆单
     * @return
     */
    @PostMapping("/orderSplit")
    public List<WareChildOrderVo> orderSplit(OrderWareMapVo params){
        log.info("订单执行拆单:{}",params);
        //把这个大订单，拆分成两个子订单（保存数据库）
        List<WareChildOrderVo> wareChildOrderVos = orderBizService.orderSplit(params);
        System.out.println(wareChildOrderVos);
        return wareChildOrderVos;
    }
}
