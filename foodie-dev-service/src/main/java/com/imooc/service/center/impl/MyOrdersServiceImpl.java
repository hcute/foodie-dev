package com.imooc.service.center.impl;

import com.github.pagehelper.PageHelper;
import com.imooc.enums.OrderStatusEnum;
import com.imooc.enums.YesOrNo;
import com.imooc.mapper.OrderStatusMapper;
import com.imooc.mapper.OrdersMapper;
import com.imooc.mapper.OrdersMapperCustom;
import com.imooc.pojo.OrderStatus;
import com.imooc.pojo.Orders;
import com.imooc.pojo.vo.MyOrdersVO;
import com.imooc.service.center.MyOrdersService;
import com.imooc.utils.PagedGridResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.HashMap;
import java.util.List;


@Service
public class MyOrdersServiceImpl extends BasicService implements MyOrdersService {

    @Autowired
    private OrdersMapperCustom ordersMapperCustom;

    @Autowired
    private OrderStatusMapper orderStatusMapper;


    @Autowired
    private OrdersMapper ordersMapper;

    @Override
    public PagedGridResult queryByOrders(String userId, Integer orderStatus, Integer page, Integer pageSize) {
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("userId",userId);
        if (orderStatus != null) {
            paramsMap.put("orderStatus",orderStatus);
        }
        PageHelper.startPage(page,pageSize);
        List<MyOrdersVO> list = ordersMapperCustom.queryMyOrders(paramsMap);
        return setterPagedGird(list,page);
    }


    @Transactional(propagation= Propagation.REQUIRED)
    @Override
    public void updateDeliverOrderStatus(String orderId) {

        OrderStatus updateOrder = new OrderStatus();
        updateOrder.setOrderStatus(OrderStatusEnum.WAIT_RECEIVE.type);
        updateOrder.setDeliverTime(new Date());

        Example example = new Example(OrderStatus.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orderId", orderId);
        criteria.andEqualTo("orderStatus", OrderStatusEnum.WAIT_DELIVER.type);

        orderStatusMapper.updateByExampleSelective(updateOrder, example);
    }


    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Orders queryMyOrder(String userId, String orderId) {

        Orders orders = new Orders();
        orders.setIsDelete(YesOrNo.NO.type);
        orders.setUserId(userId);
        orders.setId(orderId);
        return ordersMapper.selectOne(orders);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public boolean updateReceiveOrderStatus(String orderId) {
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderStatus(OrderStatusEnum.SUCCESS.type);
        orderStatus.setSuccessTime(new Date());
        Example example = new Example(OrderStatus.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orderId",orderId);
        criteria.andEqualTo("orderStatus",OrderStatusEnum.WAIT_RECEIVE.type);
        int result = orderStatusMapper.updateByExampleSelective(orderStatus, example);
        return result == 1 ? true:false;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public boolean deleteOrder(String userId, String orderId) {
        Orders orders = new Orders();
        orders.setIsDelete(YesOrNo.YES.type);
        orders.setUpdatedTime(new Date());
        Example example = new Example(Orders.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id",orderId);
        criteria.andEqualTo("userId",userId);
        int result = ordersMapper.updateByExampleSelective(orders, example);
        return result == 1 ? true:false;
    }


}
