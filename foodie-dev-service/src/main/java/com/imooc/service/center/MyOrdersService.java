package com.imooc.service.center;

import com.imooc.utils.PagedGridResult;

public interface MyOrdersService {


    /**
     * 查询我的订单列表
     * @param userId
     * @param orderStatus
     * @param page
     * @param pageSize
     * @return
     */
    PagedGridResult queryByOrders(String userId,Integer orderStatus,
                                  Integer page,Integer pageSize);


    /**
     * 修改待发货为已发货
     * @param orderId
     */
    void updateDeliverOrderStatus(String orderId);
}
