package com.imooc.service.impl;

import com.imooc.service.StuService;
import com.imooc.service.TestTransService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TestTransServiceImpl implements TestTransService {
    @Autowired
    private StuService stuService;

    /**
     * 事务的传播Propagation
     *  REQUIRED: 如果当前没有事务，则自己新建事务，如果当前存在事务，则加入这个事务。子方法必须运行在事务中
     *      领导没饭吃，我有钱，我自己买着吃，领导有饭吃，会分给你一起吃
     *  SUPPORTS: 如果当前有事务，则使用事务，如果当前没有事务，则不使用事务
     *      领导没饭吃，我也没饭吃，领导有饭吃，我也有饭吃
     *  MANDATORY: 该属性强制必须存在一个事务，如果不存在，则抛出异常
     *      领导必须管饭，不管饭我就不乐意了，就不干了
     *  REQUIRES_NEW: 如果当前有事务，则挂起当前事务，创建新的事务给自己使用，当前没有事务，同REQUIRED
     *      领导有饭吃，我不要，自己买了自己吃
     *  NOT_SUPPORTED: 如果当前有事务，则把事务挂起，自己不使用事务执行数据库操作
     *      领导有饭吃，分一点给你，我太忙了，放一边，我不吃
     *  NEVER: 如果当前有事务，则抛出异常。
     *      领导有饭给你吃，我不想吃，我热爱工作，我抛出异常
     *  NESTED: 如果当前有事务，则开启一个嵌套事务，如果没有事务，则新建事务执行
     *      嵌套事务：嵌套事务是单独提交或者回滚
     *               即：父事务提交，则会携带子事务提交，父事务回滚，则子事务回滚，
     *               相反，子事务发生异常回滚，父事务可以选择提交或者回滚，如果不处理子事务中的异常会一起回滚，如果处理子事务中的异常，
     *               则父事务可以选择回滚和不回滚
     *      领导决策不对，老板怪罪，领导带着小弟一同受罪，小弟出了差错，领导可以推卸责任
     */


    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void testPropagationTrans() {
        stuService.saveParent();

        try {
            // save point
            stuService.saveChildren();
        } catch (Exception e) {
            e.printStackTrace();
        }

//        int a = 1 / 0;

    }
}
