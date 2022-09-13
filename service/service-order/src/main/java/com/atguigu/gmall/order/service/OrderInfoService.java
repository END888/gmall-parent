package com.atguigu.gmall.order.service;

import com.atguigu.gmall.model.order.OrderInfo;
import com.atguigu.gmall.model.vo.order.OrderSubmitVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author ycy
* @description 针对表【order_info(订单表 订单表)】的数据库操作Service
* @createDate 2022-09-11 10:14:36
*/
public interface OrderInfoService extends IService<OrderInfo> {

    /**
     * 根据页面提交的数据生成一个数据库的订单
     * @param submitVo
     * @return
     */
    Long saveOrder(OrderSubmitVo submitVo,String tradeNo);
}
