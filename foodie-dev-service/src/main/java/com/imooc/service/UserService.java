package com.imooc.service;

import com.imooc.pojo.Users;
import com.imooc.pojo.bo.UserBO;

public interface UserService {

    /**
     * 判断用户名是否存在
     * @param username
     * @return
     */
    boolean queryUsernameIsExist(String username);

    /**
     * 创建用户
     * @param userBO
     * @return
     */
    Users createUser(UserBO userBO);

    /**
     * 检索用户名和密码是否匹配
     * @param username
     * @param password
     * @return
     */
    Users queryUserForLogin(String username,String password);
}
