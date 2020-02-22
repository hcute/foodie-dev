package com.imooc.service.impl;

import com.imooc.enums.YesOrNo;
import com.imooc.mapper.UserAddressMapper;
import com.imooc.pojo.UserAddress;
import com.imooc.pojo.bo.AddressBO;
import com.imooc.service.AddressService;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private UserAddressMapper userAddressMapper;

    @Autowired
    private Sid sid;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<UserAddress> queryAll(String userId) {
        UserAddress ua = new UserAddress();
        ua.setUserId(userId);
        return userAddressMapper.select(ua);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void addNewUserAddress(AddressBO addressBO) {
        // 1. 判断当前用户是否存在地址，则新增为默认地址

        List<UserAddress> userAddresses = this.queryAll(addressBO.getUserId());
        Integer isDefault = 0;
        if (userAddresses == null || userAddresses.isEmpty() || userAddresses.size() == 0) {
            isDefault = 1;
        }
        String addressId = sid.nextShort();
        // 2. 保存数据库地址
        UserAddress newAddress = new UserAddress();
        BeanUtils.copyProperties(addressBO,newAddress);
        newAddress.setId(addressId);
        newAddress.setIsDefault(isDefault);
        newAddress.setCreatedTime(new Date());
        newAddress.setUpdatedTime(new Date());
        userAddressMapper.insert(newAddress);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateUserAddress(AddressBO addressBO) {
        UserAddress updateAddress = new UserAddress();
        BeanUtils.copyProperties(addressBO,updateAddress);
        updateAddress.setId(addressBO.getAddressId());
        updateAddress.setUpdatedTime(new Date());
        userAddressMapper.updateByPrimaryKeySelective(updateAddress);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void deleteUserAddress(String userId, String addressId) {
        UserAddress deleteAddress = new UserAddress();
        deleteAddress.setUserId(userId);
        deleteAddress.setId(addressId);
        userAddressMapper.delete(deleteAddress);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void setAddressToDefault(String userId, String addressId) {
        // 1. 将用户地址都改为不是默认地址

        UserAddress queryAddress = new UserAddress();
        queryAddress.setUserId(userId);
        queryAddress.setIsDefault(YesOrNo.YES.type);
        List<UserAddress> userAddresses = userAddressMapper.select(queryAddress);
        for (UserAddress userAddress : userAddresses) {
            if (userAddress.getIsDefault().equals(YesOrNo.YES.type)){
                userAddress.setIsDefault(YesOrNo.NO.type);
            }
            userAddressMapper.updateByPrimaryKeySelective(userAddress);
        }

        // 2. 设置当前地址为默认地址
        UserAddress defaultAddress = new UserAddress();
        defaultAddress.setIsDefault(YesOrNo.YES.type);
        defaultAddress.setUserId(userId);
        defaultAddress.setId(addressId);
        userAddressMapper.updateByPrimaryKeySelective(defaultAddress);


    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public UserAddress queryUserAddress(String userId, String addressId) {
        UserAddress userAddress = new UserAddress();
        userAddress.setUserId(userId);
        userAddress.setId(addressId);
        UserAddress dependingUserAddress = userAddressMapper.selectOne(userAddress);
        return dependingUserAddress;
    }
}
