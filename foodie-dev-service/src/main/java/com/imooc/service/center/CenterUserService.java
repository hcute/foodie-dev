package com.imooc.service.center;

import com.imooc.pojo.Users;
import com.imooc.pojo.bo.center.CenterUserBO;

public interface CenterUserService {
    /**
     *  根据用户id查询用户信息
     * @param userId
     * @return
     */
    Users queryUserInfo(String userId);


    /**
     * 修改用户信息
     * @param userId
     * @param centerUserBO
     */
    Users updateUserInfo(String userId, CenterUserBO centerUserBO);

    /**
     * 更新用户图像
     * @param userId
     * @param faceUrl
     * @return
     */
    Users updateUserFaces(String userId,String faceUrl);
}
