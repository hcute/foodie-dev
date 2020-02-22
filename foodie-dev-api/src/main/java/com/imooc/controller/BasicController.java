package com.imooc.controller;

public class BasicController {

    public static final String FOODIE_SHOPCAT = "shopcat";

    public static final Integer COMMON_PAGE_SIZE = 10;

    public static final Integer PAGE_SIZE = 20;

    // 支付中心的调用地址
    String paymentUrl = "http://payment.t.mukewang.com/foodie-payment/payment/createMerchantOrder";

    // 支付中心通知天天吃货平台的的回调的URL
    String payReturnUrl = "http://ykub7d.natappfree.cc/orders/notifyMerchantOrderPaid";
}
