package com.imooc.service;

import com.imooc.pojo.UserAddress;
import com.imooc.pojo.bo.AddressBO;

import java.util.List;

public interface AddressService {

    List<UserAddress> queryAll(String userId);

    /**
     * 新增用户地址
     * @param addressBO
     */
    void addNewUserAddress(AddressBO addressBO);

    /**
     * 修改用户地址
     * @param addressBO
     */
    void updateUserAddress(AddressBO addressBO);

    /**
     * 根据用户id和addressId 删除用户地址
     * @param userId
     * @param addressId
     */
    void deleteUserAddress(String userId,String addressId);

    /**
     * 将地址设置为用户的默认地址
     * @param userId
     * @param addressId
     */
    void setAddressToDefault(String userId,String addressId);

    /**
     * 根据用户id 和地址id查询用户地址信息
     * @param userId
     * @param addressId
     * @return
     */
    UserAddress queryUserAddress(String userId,String addressId);
}
