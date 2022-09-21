package com.atguigu.gmall.model.vo.order;

import lombok.Data;

@Data
public class OrderWareMapVo {
    private Long orderId;
    private String wareSkuMap; //json 是 OrderWareMapSkuItemVo 的集合
}
