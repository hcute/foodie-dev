package com.imooc.service.center;

import com.imooc.pojo.OrderItems;
import com.imooc.pojo.bo.center.OrderItemsCommentBO;
import com.imooc.utils.PagedGridResult;

import java.util.List;

public interface MyCommentService {
    /**
     * 查询订单关联商品
     * @param orderId
     * @return
     */
    List<OrderItems> queryPendingComment(String orderId);


    /**
     * 保存商品评价
     * @param userId
     * @param orderId
     * @param commentList
     */
    void saveComments(String userId, String orderId, List<OrderItemsCommentBO> commentList);

    /**
     * 查询我的评价
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    PagedGridResult queryMyComments(String userId,Integer page,Integer pageSize);
}
