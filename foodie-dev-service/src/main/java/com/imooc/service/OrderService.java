package com.imooc.service;

import com.imooc.pojo.OrderStatus;
import com.imooc.pojo.bo.SubmitOrderBo;
import com.imooc.pojo.vo.OrderVO;

public interface OrderService {

    /**
     * 创建订单信息
     * @param submitOrderBo
     */
    OrderVO createOrder(SubmitOrderBo submitOrderBo);


    /**
     * 修改订单状态
     * @param orderId
     * @param orderStatus
     */
    void updateOrderStatus(String orderId,Integer orderStatus);

    /**
     * 查询订单状态
     * @param orderId
     * @return
     */
    OrderStatus queryOrderStatusInfo(String orderId);

    /**
     * 关闭超时未支付订单
     */
    void closeOrder();
}
