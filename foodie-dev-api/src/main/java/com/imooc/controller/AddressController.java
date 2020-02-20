package com.imooc.controller;


import com.imooc.pojo.UserAddress;
import com.imooc.pojo.bo.AddressBO;
import com.imooc.service.AddressService;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.MobileEmailUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "地址相关",tags = {"地址相关的api接口"})
@RequestMapping("/address")
@RestController
public class AddressController {

    /**
     * 用户在确认订单页面，可以针对收货地址作如下操作
     * 1，查询用户的所有收获地址列表
     * 2，新增收货地址
     * 3，删除收货地址
     * 4，编辑收货地址
     * 5，设置默认收货地址
     */

    @Autowired
    private AddressService addressService;


    @ApiOperation(value = "获取用户的收货地址",notes = "获取用户的收货地址",httpMethod = "POST")
    @PostMapping("/list")
    public IMOOCJSONResult list(
            @ApiParam(name = "userId",value = "用户id",required = true)
            @RequestParam String userId){

        if (StringUtils.isBlank(userId)){
            return IMOOCJSONResult.errorMsg("参数不能为空");
        }

        List<UserAddress> userAddresses = addressService.queryAll(userId);

        return IMOOCJSONResult.ok(userAddresses);
    }


    @ApiOperation(value = "用户新增地址",notes = "用户新增地址",httpMethod = "POST")
    @PostMapping("/add")
    public IMOOCJSONResult add(
            @RequestBody AddressBO addressBO){

        IMOOCJSONResult imoocjsonResult = checkAddress(addressBO);
        if (imoocjsonResult.getStatus() != 200) {
            return imoocjsonResult;
        }
        addressService.updateUserAddress(addressBO);
        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "用户修改地址",notes = "用户修改地址",httpMethod = "POST")
    @PostMapping("/update")
    public IMOOCJSONResult update(
            @RequestBody AddressBO addressBO){

        if (StringUtils.isBlank(addressBO.getAddressId())){
            return IMOOCJSONResult.errorMsg("修改地址错误，addressId不能为空");
        }
        IMOOCJSONResult imoocjsonResult = checkAddress(addressBO);
        if (imoocjsonResult.getStatus() != 200) {
            return imoocjsonResult;
        }
        addressService.updateUserAddress(addressBO);
        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "删除用户地址",notes = "删除用户地址",httpMethod = "POST")
    @PostMapping("/delete")
    public IMOOCJSONResult delete(
            @ApiParam(name = "userId",value = "用户ID",required = true)
            @RequestParam String userId,
            @ApiParam(name = "addressId",value = "地址id",required = true)
            @RequestParam String addressId){

        if (StringUtils.isBlank(userId) || StringUtils.isBlank(addressId)){
            return IMOOCJSONResult.errorMsg("删除地址错误，userId或addressId不能为空");
        }

        addressService.deleteUserAddress(userId,addressId);
        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "用户设置默认地址",notes = "用户设置默认地址",httpMethod = "POST")
    @PostMapping("/setDefalut")
    public IMOOCJSONResult setDefalut(
            @ApiParam(name = "userId",value = "用户ID",required = true)
            @RequestParam String userId,
            @ApiParam(name = "addressId",value = "地址id",required = true)
            @RequestParam String addressId){

        if (StringUtils.isBlank(userId) || StringUtils.isBlank(addressId)){
            return IMOOCJSONResult.errorMsg("用户设置默认地址错误，userId或addressId不能为空");
        }

        addressService.setAddressToDefault(userId,addressId);
        return IMOOCJSONResult.ok();
    }

    private IMOOCJSONResult checkAddress(AddressBO addressBO) {
        String receiver = addressBO.getReceiver();
        if (StringUtils.isBlank(receiver)) {
            return IMOOCJSONResult.errorMsg("收货人不能为空");
        }
        if (receiver.length() > 12) {
            return IMOOCJSONResult.errorMsg("收货人姓名不能太长");
        }

        String mobile = addressBO.getMobile();
        if (StringUtils.isBlank(mobile)) {
            return IMOOCJSONResult.errorMsg("收货人手机号不能为空");
        }
        if (mobile.length() != 11) {
            return IMOOCJSONResult.errorMsg("收货人手机号长度不正确");
        }
        boolean isMobileOk = MobileEmailUtils.checkMobileIsOk(mobile);
        if (!isMobileOk) {
            return IMOOCJSONResult.errorMsg("收货人手机号格式不正确");
        }

        String province = addressBO.getProvince();
        String city = addressBO.getCity();
        String district = addressBO.getDistrict();
        String detail = addressBO.getDetail();
        if (StringUtils.isBlank(province) ||
                StringUtils.isBlank(city) ||
                StringUtils.isBlank(district) ||
                StringUtils.isBlank(detail)) {
            return IMOOCJSONResult.errorMsg("收货地址信息不能为空");
        }

        return IMOOCJSONResult.ok();
    }

}
