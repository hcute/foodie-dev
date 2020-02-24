package com.imooc.service.center.impl;

import com.github.pagehelper.PageInfo;
import com.imooc.utils.PagedGridResult;

import java.util.List;

public class BasicService {

    public PagedGridResult setterPagedGird(List<?> list, Integer page) {
        PageInfo<?> pageList = new PageInfo<>(list);
        PagedGridResult grid = new PagedGridResult();
        grid.setPage(page);
        grid.setRows(list);
        grid.setTotal(pageList.getPages());
        grid.setRecords(pageList.getTotal());
        return grid;
    }
}
