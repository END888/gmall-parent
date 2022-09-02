package com.atguigu.gmall.common.retry;

import feign.RetryableException;
import feign.Retryer;

/**
 * 幂等性： 做一次和做很多次效果一样。
 *   - select/update/delete： 天然幂等
 *   - insert：不幂等，每次操作都会导致数据库保存新数据进去。
 *   - 关闭feign重试功能；特殊业务放大读取超时，连接超时不用管。
 *
 * 自定义feign重试次数逻辑
 */
public class MyRetry implements Retryer {

    private int cur;
    private int max;

    public MyRetry(){
        this.cur = 0;
        this.max = 2;
    }

    /**
     * 关闭 feign 的重试
     * @param e
     */
    @Override
    public void continueOrPropagate(RetryableException e) {
//        throw e;

    }

    @Override
    public Retryer clone() {
        return this;
    }
}
