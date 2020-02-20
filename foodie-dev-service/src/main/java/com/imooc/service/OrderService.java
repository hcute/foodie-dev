package com.imooc.service;

import com.imooc.pojo.bo.SubmitOrderBo;

public interface OrderService {

    /**
     * 创建订单信息
     * @param submitOrderBo
     */
    void createOrder(SubmitOrderBo submitOrderBo);
}
