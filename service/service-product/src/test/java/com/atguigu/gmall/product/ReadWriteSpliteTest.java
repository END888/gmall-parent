package com.atguigu.gmall.product;

import com.atguigu.gmall.model.product.BaseTrademark;
import com.atguigu.gmall.product.mapper.BaseTrademarkMapper;
import org.apache.shardingsphere.infra.hint.HintManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ReadWriteSpliteTest {

    @Autowired
    BaseTrademarkMapper baseTrademarkMapper;

    @Test
    public void testW(){
        BaseTrademark trademark = baseTrademarkMapper.selectById(4L);
        System.out.println("trademark = " + trademark);

        trademark.setTmName("小米2");
        baseTrademarkMapper.updateById(trademark);

        // 改完后，再去查询，很有可能查不到最新结果

        HintManager.getInstance().setWriteRouteOnly(); // 强制走主库
        trademark = baseTrademarkMapper.selectById(4L);
        System.out.println("改完后查到的：trademark2 = " + trademark);

    }
}
