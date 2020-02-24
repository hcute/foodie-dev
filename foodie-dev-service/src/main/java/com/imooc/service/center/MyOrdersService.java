package com.imooc.service.center;

import com.imooc.pojo.Orders;
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


    /**
     * 查询用户的订单信息
     * @param userId
     * @param orderId
     * @return
     */
    Orders queryMyOrder(String userId,String orderId);

    /**
     * 跟新订单状态为确认收货
     * @param orderId
     * @return
     */
    boolean updateReceiveOrderStatus(String orderId);

    /**
     * 删除订单
     * @param userId
     * @param orderId
     * @return
     */
    boolean deleteOrder(String userId,String orderId);


}
