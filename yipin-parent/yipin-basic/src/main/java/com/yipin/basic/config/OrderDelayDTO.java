package com.yipin.basic.config;


import lombok.Data;

import java.util.Date;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**订单延时队列**/
@Data
public class OrderDelayDTO implements Delayed {
    /**订单id**/
    private String orderId;
    /**过期时间**/
    private Date expirationTime;

    /**判断策略：过期时间大于当前时间时就算过期**/
    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(this.expirationTime.getTime() - System.currentTimeMillis(),TimeUnit.NANOSECONDS);
    }

    /**订单加入队列的排序规则**/
    @Override
    public int compareTo(Delayed o) {
        OrderDelayDTO orderDelayDto = (OrderDelayDTO) o;
        long time = orderDelayDto.getExpirationTime().getTime();
        long time1 = this.getExpirationTime().getTime();
        return time == time1 ? 0 : time < time1 ? 1 : -1;
    }
}
