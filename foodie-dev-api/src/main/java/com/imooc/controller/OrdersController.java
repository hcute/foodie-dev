package com.imooc.controller;


import com.imooc.enums.OrderStatusEnum;
import com.imooc.enums.PayMethod;
import com.imooc.pojo.bo.SubmitOrderBo;
import com.imooc.pojo.vo.MerchantOrdersVO;
import com.imooc.pojo.vo.OrderVO;
import com.imooc.service.OrderService;
import com.imooc.utils.IMOOCJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(value = "订单相关",tags = {"订单相关的api接口"})
@RequestMapping("/orders")
@RestController
public class OrdersController extends BasicController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private RestTemplate restTemplate;

    @ApiOperation(value = "获取用户的收货地址",notes = "获取用户的收货地址",httpMethod = "POST")
    @PostMapping("/create")
    public IMOOCJSONResult create(@RequestBody SubmitOrderBo submitOrderBo, HttpServletRequest request,
                                  HttpServletResponse response){


        if (submitOrderBo.getPayMethod() != PayMethod.WEIXIN.type
                && submitOrderBo.getPayMethod() != PayMethod.ALIPAY.type){
            IMOOCJSONResult.errorMsg("不支持的支付方式");
        }

        System.out.println(submitOrderBo);

        // 1. 创建订单
        OrderVO orderVO = orderService.createOrder(submitOrderBo);

        String orderId = orderVO.getOrderId();


        // 2. 创建订单后，移除购物车已提交的商品
        /**
         * 比如购物车中的商品
         * 1001
         * 2002 - 选择购买
         * 3003 - 选择购买
         * 4004
         */
        // 2.1 剔除后端的cookie
            //TODO 整合redis后，完善购物车中已结算的商品的清除，并且同步到前端cookie
        // 2.2 清除前端的cookie- 现在把所有购物车清空
//        CookieUtils.setCookie(request,response,FOODIE_SHOPCAT, "",true);

        // 3. 向支付中心发送当前订单用于保存支付中心的订单数据
        MerchantOrdersVO merchantOrdersVO = orderVO.getMerchantOrdersVO();
        merchantOrdersVO.setReturnUrl(payReturnUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("imoocUserId","6670500-631410114");
        headers.add("password","wdip-dw0o-3k4m-t0or");

        HttpEntity<MerchantOrdersVO> entity = new HttpEntity<>(merchantOrdersVO,headers);

        ResponseEntity<IMOOCJSONResult> responseEntity =
                restTemplate.postForEntity(paymentUrl, entity, IMOOCJSONResult.class);

        IMOOCJSONResult paymentResult = responseEntity.getBody();

        if (paymentResult.getStatus() != 200) {
            return IMOOCJSONResult.errorMsg("支付中心订单创建失败，请联系管理员");
        }

        return IMOOCJSONResult.ok(orderId);
    }

    @ApiIgnore
    @PostMapping("notifyMerchantOrderPaid")
    public Integer notifyMerchantOrderPaid(String merchantOrderId) {

        orderService.updateOrderStatus(merchantOrderId, OrderStatusEnum.WAIT_DELIVER.type);

        return HttpStatus.OK.value();


    }


    @ApiIgnore
    @PostMapping("getPaidOrderInfo")
    public IMOOCJSONResult getPaidOrderInfo(String orderId) {

        return IMOOCJSONResult.ok(orderService.queryOrderStatusInfo(orderId));

    }


}
