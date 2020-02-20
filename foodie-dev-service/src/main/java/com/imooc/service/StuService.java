package com.imooc.service;

import com.imooc.pojo.Stu;

public interface StuService {

    Stu getStuInfo(Integer id);

    void saveStu();

    void updateStu(Integer id);

    void deleteStu(Integer id);

    void saveParent();

    void saveChildren();

}
