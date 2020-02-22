package com.imooc.config;

import com.imooc.service.OrderService;
import com.imooc.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OrderJob {
    /**
     * 定时任务的弊端
     * 1，会有时间差，程序不严谨
     *  假如订单超过1小时关闭，比如9:35下单 ，10 点的时候轮询没有超时，11点轮询超时，但是时间差为25分钟
     * 2，不支持集群
     *  使用集群会有多个定时任务在执行
     *  解决方案：
     *      只是用一个节点，单独来运行定时任务
     * 3，会对数据库全表搜索，会影响数据库性能
     *
     * 总结：定时任务只使用于轻量级小型任务，后续可以使用消息队列来解决
     *      MQ
     *          RabbitMQ
     *          RocketMQ
     *          Kafka
     *          ZeroMQ
     *      使用延时队列
     */

    @Autowired
    private OrderService orderService;

//    @Scheduled(cron = "0/3 * * * * ?")
    @Scheduled(cron = "0 0 0/1 * * ? ")
    public void autoCloseOrder(){
        System.out.println("执行定时任务当前的时间为：" + DateUtil.getCurrentDateString(DateUtil.DATETIME_PATTERN));
        orderService.closeOrder();

    }
}
