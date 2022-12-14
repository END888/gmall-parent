package com.atguigu.gmall.web.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.feign.seckill.SeckillFeignClient;
import com.atguigu.gmall.model.activity.SeckillGoods;
import com.atguigu.gmall.model.vo.seckill.SeckillOrderConfirmVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 秒杀
 */
@Controller
public class SeckillController {

    @Autowired
    SeckillFeignClient seckillFeignClient;

    /**
     * 秒杀列表页
     * @param model
     * @return
     */
    @GetMapping("/seckill.html")
    public String seckillPage(Model model){
        // TODO 查询秒杀数据
        // {skuId、skuDefaultImg、skuName、price、costPrice、num、stockCount}
        Result<List<SeckillGoods>> goodsList = seckillFeignClient.getCurrentDaySeckillGoodsList();
        model.addAttribute("list",goodsList.getData());
        return "seckill/index";
    }

    /**
     * 秒杀商品详情页
     * @param model
     * @param skuId
     * @return
     */
    @GetMapping("/seckill/{skuId}.html")
    public String seckillDetail(Model model, @PathVariable("skuId") Long skuId){
        Result<SeckillGoods> good = seckillFeignClient.getSeckillGood(skuId);
        model.addAttribute("item",good.getData());
        return "seckill/item";
    }

    /**
     * 秒杀排队页
     * ?skuId=49&skuIdStr=a3117efeff2ff9fd94906f2995565182
     * @param skuId
     * @param skuIdStr
     * @param model
     * @return
     */
    @GetMapping("/seckill/queue.html")
    public String seckillQueue(@RequestParam("skuId") Long skuId,
                               @RequestParam("skuIdStr") String skuIdStr,
                               Model model){
        model.addAttribute("skuId",skuId);
        model.addAttribute("skuIdStr",skuIdStr);
        return "seckill/queue";
    }

    @GetMapping("/seckill/trade.html")
    public String trade(Model model,@RequestParam("skuId")Long skuId){
        // 返回的是订单确认页的数据
        SeckillOrderConfirmVo confirmVo = seckillFeignClient.getSeckillOrderConfirmVo(skuId).getData();
        model.addAttribute("detailArrayList",confirmVo.getTempOrder().getOrderDetailList());
        model.addAttribute("userAddressList",confirmVo.getUserAddressList());
        model.addAttribute("totalNum",confirmVo.getTempOrder().getOrderDetailList().size());
        model.addAttribute("totalAmount",confirmVo.getTempOrder().getTotalAmount());
        return  "seckill/trade";
    }
}
