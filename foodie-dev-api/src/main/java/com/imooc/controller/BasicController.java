package com.imooc.controller;

import java.io.File;

public class BasicController {

    public static final String FOODIE_SHOPCAT = "shopcat";

    public static final Integer COMMON_PAGE_SIZE = 10;

    public static final Integer PAGE_SIZE = 20;

    // 支付中心的调用地址
    String paymentUrl = "http://payment.t.mukewang.com/foodie-payment/payment/createMerchantOrder";

    // 支付中心通知天天吃货平台的的回调的URL
    String payReturnUrl = "http://ykub7d.natappfree.cc/orders/notifyMerchantOrderPaid";

    // 用户上传图像的地址
//    public static final String IMAGE_USER_FACE_LOCATION =
//            "/Users/hujianchen/Documents/learn/mooc_framework/code/foodie-dev/faces";

    public static final String IMAGE_USER_FACE_LOCATION =
                    File.separator + "Users" +
                    File.separator + "hujianchen" +
                    File.separator+"Documents" +
                    File.separator+"learn" +
                    File.separator+"mooc_framework" +
                    File.separator+"code" +
                    File.separator+"foodie-dev" +
                    File.separator+"images" +
                    File.separator+"foodie" +
                    File.separator+"faces";


}
